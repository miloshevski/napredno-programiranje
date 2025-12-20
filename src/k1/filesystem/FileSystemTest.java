package k1.filesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface IFile extends Comparable<IFile>{
    String getFileName();
    long getFileSize();
    String getFileInfo(int indent);
    void sortBySize();
    long findLargestFile();
}

class IndentPrinter{
    public static String printIndent(int indentLevel){
        return IntStream.range(0, indentLevel)
                .mapToObj(i -> "    ")
                .collect(Collectors.joining());
    }
}

class File implements IFile{
    protected String fileName;
    protected long fileSize;

    public File(String fileName, long fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public File(String fileName) {
        this.fileName = fileName;
        this.fileSize = 0L;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    @Override
    public String getFileInfo(int indent) {
        return String.format("%sFile name: %10s File size: %10d\n",
                IndentPrinter.printIndent(indent),
                getFileSize(),
                getFileSize());
    }

    @Override
    public void sortBySize() {
        return;
    }

    @Override
    public long findLargestFile() {
        return this.fileSize;
    }

    @Override
    public int compareTo(IFile o) {
        return Long.compare(this.getFileSize(),o.getFileSize());
    }


}

class Folder extends File implements IFile{
    List<IFile> files;

    public Folder(String fileName){
        super(fileName);
        files = new ArrayList<>();
    }
    private boolean namePresent(String name){
        IFile f = files.stream().filter(ff -> ff.getFileName().equals(name)).findFirst().orElse(null);
        return f != null;
    }
    public void addFile(IFile file) throws FileNameExistsException {
        if(namePresent(file.getFileName())){
            throw new FileNameExistsException(file.getFileName(),this.fileName);
        }
        files.add(file);
    }

    @Override
    public long getFileSize() {
        return files.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public String getFileInfo(int indent) {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%sFolder name: %10s Folder size: %10d\n",
                IndentPrinter.printIndent(indent),
                fileName,
                this.getFileSize()));

        files.stream().forEach(file -> sb.append(file.getFileInfo(indent + 1)));
        return sb.toString();
    }
}

class FileNameExistsException extends Exception{
    public FileNameExistsException(String fileName, String foldername) {
        super(String.format("There is already a file named %s in the folder %s",fileName, foldername));
    }
}
class FileSystem{
    Folder root;
    public FileSystem(){
        root = new Folder("root");
    }
    void addFile(IFile file) throws FileNameExistsException {
        root.addFile(file);
    }
    long findLargestFile(){
        return root.findLargestFile();
    }

    void sortBySize(){
        root.sortBySize();
    }

    @Override
    public String toString() {
        return this.root.getFileInfo(0);
    }
}

public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}