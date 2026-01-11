package posttester;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface IComment {
    String username();
    String commentId();
    String content();
    int likes();
    List<IComment> replies();
    void addComment(IComment comment);
    void like();
    String print(int indent);
    int getLikes();
}

class LeafComment implements IComment{
    private String username;
    private String commentId;
    private String content;
    private int likes;

    public LeafComment(String username, String commentId, String content) {
        this.username = username;
        this.commentId = commentId;
        this.content = content;
        likes = 0;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String commentId() {
        return commentId;
    }

    @Override
    public String content() {
        return content;
    }

    @Override
    public int likes() {
        return likes;
    }

    @Override
    public List<IComment> replies() {
        return Collections.emptyList();
    }

    @Override
    public void addComment(IComment comment) {
        return;
    }

    @Override
    public void like() {
        likes++;
    }

    @Override
    public String print(int indent) {
        String ind = " ".repeat(indent);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%sComment: %s%n",ind,content))
                .append(String.format("%sWritten by: %s%n",ind,username))
                .append(String.format("%sLikes: %d%n",ind,likes));
        return sb.toString();
    }

    @Override
    public int getLikes() {
        return likes;
    }
}

class Comment implements IComment{
    private String username;
    private String commentId;
    private String content;
    private int likes;
    private List<IComment> replies;

    public Comment(String username, String commentId, String content) {
        this.username = username;
        this.commentId = commentId;
        this.content = content;
        likes = 0;
        replies = new ArrayList<>();
    }


    @Override
    public String username() {
        return username;
    }

    @Override
    public String commentId() {
        return commentId;
    }

    @Override
    public String content() {
        return content;
    }

    @Override
    public int likes() {
        int sum = likes;
        for(IComment c : replies){
            sum += c.likes();
        }
        return sum;
    }

    @Override
    public List<IComment> replies() {
        return replies;
    }

    @Override
    public void addComment(IComment comment) {
        replies.add(comment);
    }

    @Override
    public void like() {
        likes++;
    }

    @Override
    public String print(int indent) {
        String ind = " ".repeat(indent);
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%sComment: %s%n", ind, content))
                .append(String.format("%sWritten by: %s%n", ind, username))
                .append(String.format("%sLikes: %d%n", ind, getLikes()));

        Comparator<IComment> cmp = Comparator.comparing(IComment::likes).reversed();

        for(IComment c : replies.stream().sorted(cmp).collect(Collectors.toList())){
            sb.append(c.print(indent + 4));
        }
        return sb.toString();
    }

    @Override
    public int getLikes() {
        return likes;
    }
}

class Post{
    private String username;
    private String postContent;
    private List<IComment> comments = new ArrayList<>();
    private Map<String,IComment> map = new HashMap<>();

    public Post(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;
    }

    public void addComment(String username, String commentId, String content, String replyToId){
        IComment comment = new Comment(username,commentId,content);
        map.put(commentId,comment);
        if(replyToId == null){
            comments.add(comment);
        }else {
            IComment parent = map.get(replyToId);
            if(parent != null){
                parent.addComment(comment);
            }else{
                comments.add(comment);
            }
        }
    }
    public void likeComment(String commentId){
        map.get(commentId).like();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Post: %s%n", postContent));
        sb.append(String.format("Written by: %s%n", username));
        sb.append("Comments:\n");
        Comparator<IComment> cmp = Comparator.comparingInt(IComment::likes).reversed();

        for(IComment c : comments.stream().sorted(cmp).collect(Collectors.toList())){
            sb.append(c.print(8));
        }
        return sb.toString();
    }
}


public class PostTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }

        }
    }
}
