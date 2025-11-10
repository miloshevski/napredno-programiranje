package lab3.movietheater.network;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Ad implements Comparable<Ad>{
    private String id;
    private String category;
    private double bidValue;
    private double ctr;
    private String content;

    public Ad(String id, String category, double bidValue, double ctr, String content) {
        this.id = id;
        this.category = category;
        this.bidValue = bidValue;
        this.ctr = ctr;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getBidValue() {
        return bidValue;
    }

    public double getCtr() {
        return ctr;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int compareTo(Ad other) {
        int byBid = Double.compare(other.bidValue, this.bidValue);
        if (byBid != 0) return byBid;
        return this.id.compareTo(other.id);
    }

    @Override
    public String toString() {
        return String.format("%s %s (bid=%.2f, ctr=%.2f%%) %s",id,category,bidValue,ctr*100,content);
    }
}

class AdRequest{
    private final String id;
    private final String category;
    private final double floorBid;
    private final String keywords;

    public AdRequest(String id, String category, double floorBid, String keywords) {
        this.id = id;
        this.category = category;
        this.floorBid = floorBid;
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "%s [%s] (floor=%.2f): %s",
                id, category, floorBid, keywords);
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public double getFloorBid() {
        return floorBid;
    }

    public String getKeywords() {
        return keywords;
    }
}


class AdNetwork {
    private final List<Ad> ads = new ArrayList<>();
    private String pendingRequestLine = null;

    private int relevanceScore(Ad ad, AdRequest req) {
        int score = 0;
        if (ad.getCategory().equalsIgnoreCase(req.getCategory())) score += 10;
        String[] adWords = ad.getContent().toLowerCase().split("\\s+");
        String[] keywords = req.getKeywords().toLowerCase().split("\\s+");
        for (String kw : keywords) {
            for (String aw : adWords) {
                if (kw.equals(aw)) score++;
            }
        }
        return score;
    }

    public void readAds(BufferedReader br) throws IOException {
        ads.clear();
        String line;
        while ((line=br.readLine()) != null){
            line = line.trim();
            if(line.isEmpty()){
                continue;
            }
            if(line.startsWith("AR")){
                pendingRequestLine = line;
                break;
            }
            if(!line.startsWith("AD")){
                continue;
            }
            String[] parts = line.split("\\s+",5);
            if(parts.length < 5){
                continue;
            }
            String id = parts[0];
            String category = parts[1];
            double bid = Double.parseDouble(parts[2]);
            double ctr = Double.parseDouble(parts[3]);
            String content = parts[4];

            ads.add(new Ad(id, category, bid, ctr, content));
        }
    }
    public List<Ad> placeAds(BufferedReader br, int k, PrintWriter out) throws IOException {

        // 1. Get request line (from pending or read now)
        String reqLine = pendingRequestLine;
        if (reqLine == null) {
            reqLine = br.readLine();
            if (reqLine == null) return Collections.emptyList();
        }
        pendingRequestLine = null;

        reqLine = reqLine.trim();
        String[] parts = reqLine.split("\\s+", 4);
        String rid = parts[0];
        String rcat = parts[1];
        double floor = Double.parseDouble(parts[2]);
        String rkeys = parts.length >= 4 ? parts[3] : "";

        AdRequest request = new AdRequest(rid, rcat, floor, rkeys);

        // 2. Filter ads with bid < floor
        List<Ad> eligible = ads.stream()
                .filter(a -> a.getBidValue() >= request.getFloorBid())
                .collect(Collectors.toList());

        // scoring constants
        final double X = 5.0;
        final double Y = 100.0;

        class Scored {
            Ad ad;
            double score;
            Scored(Ad ad, double score) {
                this.ad = ad;
                this.score = score;
            }
        }

        // 3. Compute score for each ad
        List<Scored> scored = new ArrayList<>();
        for (Ad ad : eligible) {
            double total = relevanceScore(ad, request)
                    + X * ad.getBidValue()
                    + Y * ad.getCtr();
            scored.add(new Scored(ad, total));
        }

        // 4. Sort by totalScore DESC
        scored.sort((a, b) -> Double.compare(b.score, a.score));

        // take top k
        int limit = Math.min(k, scored.size());

        List<Ad> topK = scored.stream()
                .limit(limit)
                .map(s -> s.ad)
                .sorted()                 // natural order: bid desc, id asc
                .collect(Collectors.toList());

        // 5. Print result
        out.printf("Top ads for request %s:%n", request.getId());
        for (Ad ad : topK) {
            out.println(ad);
        }

        return topK;
    }

}

public class Main {
    public static void main(String[] args) throws IOException {
        AdNetwork network = new AdNetwork();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

        int k = Integer.parseInt(br.readLine().trim());

        if (k == 0) {
            network.readAds(br);
            network.placeAds(br, 1, pw);
        } else if (k == 1) {
            network.readAds(br);
            network.placeAds(br, 3, pw);
        } else {
            network.readAds(br);
            network.placeAds(br, 8, pw);
        }

        pw.flush();
    }
}