package subscriber;

import java.util.ArrayList;
import java.util.List;

interface Subscriber{
    void update(String channelName, String message);
}



interface Channel{
    void subscribe(Subscriber s);
    void unsubscribe(Subscriber s);
    void publish(String message);
}

class NewsChannel implements Channel{
    private final String name;
    private final List<Subscriber> subs = new ArrayList<>();

    NewsChannel(String name) {
        this.name = name;
    }

    @Override
    public void subscribe(Subscriber s) {
        subs.add(s);
    }

    @Override
    public void unsubscribe(Subscriber s) {
        subs.remove(s);
    }

    @Override
    public void publish(String message) {
        subs.forEach(s -> s.update(name,message));
    }
}

class NormalSubscriber implements Subscriber{
    private final String username;

    public NormalSubscriber(String username) {
        this.username = username;
    }

    @Override
    public void update(String channelName, String message) {
        System.out.printf("[NORMAL] %s got from %s: %s%n",username,channelName,message);
    }
}

abstract class SubscriberDecorator implements Subscriber{
    protected final Subscriber wrapped;

    SubscriberDecorator(Subscriber wrapped) {
        this.wrapped = wrapped;
    }
}

class LoggingSubscriber extends SubscriberDecorator{

    LoggingSubscriber(Subscriber wrapped) {
        super(wrapped);
    }

    @Override
    public void update(String channelName, String message) {
        System.out.printf("[LOG] delivering to %s...%n", wrapped.getClass().getSimpleName());
        wrapped.update(channelName, message);
        System.out.println("[LOG] delivered");
    }
}

class PrefixSubscriber extends SubscriberDecorator {
    private final String prefix;

    public PrefixSubscriber(Subscriber wrapped, String prefix) {
        super(wrapped);
        this.prefix = prefix;
    }

    @Override
    public void update(String channelName, String message) {
        wrapped.update(channelName, prefix + message);
    }
}

class KeywordFilterSubscriber extends SubscriberDecorator {
    private final String keyword;

    public KeywordFilterSubscriber(Subscriber wrapped, String keyword) {
        super(wrapped);
        this.keyword = keyword;
    }

    @Override
    public void update(String channelName, String message) {
        if (message != null && keyword != null
                && message.toLowerCase().contains(keyword.toLowerCase())) {
            wrapped.update(channelName, message);
        }
    }
}

public class ObserverDecoratorDemo {
    public static void main(String[] args) {
        Channel finki = new NewsChannel("FINKI");

        Subscriber ace = new NormalSubscriber("ace123");
        ace = new PrefixSubscriber(ace,"PREFIX: ");
        ace = new LoggingSubscriber(ace);

        finki.subscribe(ace);

        finki.publish("Raspored");
    }
}
