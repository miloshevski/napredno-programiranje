package threads;

class PrintDocument implements Runnable{

    private String documentName;

    public PrintDocument(String documentName){
        this.documentName = documentName;
    }


    @Override
    public void run() {
        System.out.println("Started printing: " + documentName +
                " | Thread: " + Thread.currentThread().getName());

        try {
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("Finished printing: " + documentName +
                " | Thread: " + Thread.currentThread().getName());
    }
}

public class PrintSystem {
    static void main(String[] args) {

        Runnable doc1 = new PrintDocument("Report.pdf");
        Runnable doc2 = new PrintDocument("Homework.docx");
        Runnable doc3 = new PrintDocument("Invoice.txt");


        Thread t1 = new Thread(doc1);
        Thread t2 = new Thread(doc2);
        Thread t3 = new Thread(doc3);

        t1.start();
        t2.start();
        t3.start();
    }
}
