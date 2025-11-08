package k1.frontpage;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

class Category implements Comparable<Category>{
    private String name;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Category o) {
        return name.compareTo(o.name);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}

abstract class NewsItem {
    protected String title;
    protected Date date;
    protected Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public abstract String getTeaser();

    @Override
    public String toString() {
        return getTeaser();
    }
}

class TextNewsItem extends NewsItem{
    String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    @Override
    public String getTeaser() {
        long duration = Calendar.getInstance().getTime().getTime() - date.getTime();

        return String.format("%s%n%d%n%.80s%n", title, TimeUnit.MILLISECONDS.toMinutes(duration), text);
    }
}

class MediaNewsItem extends NewsItem {
    private String url;
    private int views;

    public MediaNewsItem(String title, Date date, Category category,String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    public String getTeaser() {
        long duration = Calendar.getInstance().getTime().getTime() - date.getTime();

        return String.format("%s%n%d%n%s%n%d%n", title, TimeUnit.MILLISECONDS.toMinutes(duration), url, views);
    }
}

class FrontPage{
    private List<NewsItem> list;
    private Category[] categories;

    public FrontPage(Category[] categories){
        list = new ArrayList<>();
        this.categories = categories;
    }

    public void addNewsItem(NewsItem newsItem){
        list.add(newsItem);
    }

    public List<NewsItem> listByCategory(Category category){
        return list.stream().filter(i -> i.category.equals(category)).collect(Collectors.toList());
    }

    public List<NewsItem> listByCategoryName(String cateogry) throws CategoryNotFoundException {
        Category c = Arrays.stream(categories).filter(i -> i.getName().equals(cateogry)).findFirst().orElse(null);
        if(c == null){
            throw new CategoryNotFoundException(cateogry);
        }
        return listByCategory(new Category(cateogry));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        list.forEach(sb::append);

        return sb.toString();
    }
}

class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String message) {
        super(String.format("Category %s was not found",message));
    }
}


