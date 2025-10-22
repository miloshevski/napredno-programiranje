package aud2;

interface Emotion{
    String love(String name);
}

class Love implements Emotion{

    @Override
    public String love(String name) {
        return "I love "+name+" <3";
    }
}



public class InterfaceDemo {
    static void main() {
        Emotion e1 = new Love();
        Emotion e2 = new Emotion() {
            @Override
            public String love(String name) {
                return "I love you " + name;
            }
        };

        Emotion e3 = (name) -> "I love " + name;

        System.out.println(e1.love("isi"));
        System.out.println(e2.love("isi"));
        System.out.println(e3.love("isi"));
    }
}