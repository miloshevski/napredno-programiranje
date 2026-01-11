package filesystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

interface IFile{
    String getFileName();
    long getFileSize();
    String getFileInfo(int indent);
    void sortBySize();
    long findLargestFile();
}

class File implements IFile{
    private String name;
    private long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return name;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    @Override
    public String getFileInfo(int indent) {
        String ind = "    ".repeat(indent);
        return String.format("%sFile name: %10s File size: %10d%n",ind,name,size);
    }

    @Override
    public void sortBySize() {
        return;
    }

    @Override
    public long findLargestFile() {
        return size;
    }
}

class FileNameExistsException extends Exception{
    public FileNameExistsException(String file,String folder) {
        super(String.format("There is already a file named %s in the folder %s",file,folder));
    }
}

class Folder extends File implements IFile{

    private List<IFile> files;
    public Folder(String name) {
        super(name, 0);
        files = new ArrayList<>();
    }

    @Override
    public String getFileName() {
        return super.getFileName();
    }
    public void addFile(IFile file) throws FileNameExistsException {
        if(files.stream().filter(f -> f.getFileName().equals(file.getFileName())).findFirst().orElse(null) != null){
            throw new FileNameExistsException(file.getFileName(),this.getFileName());
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
        String ind = "    ".repeat(indent);
        sb.append(String.format("%sFolder name: %10s Folder size: %10d%n",ind,getFileName(),getFileSize()));
        for(IFile f : files){
            sb.append(f.getFileInfo(indent+1));
        }
        return sb.toString();
    }

    @Override
    public void sortBySize() {
        for(IFile f :files){
            f.sortBySize();
        }
        files.sort(Comparator.comparingLong(IFile::getFileSize));
    }

    @Override
    public long findLargestFile() {
        return files.stream()
                .mapToLong(IFile::findLargestFile)
                .max()
                .orElse(0);
    }
}

class FileSystem{
    Folder root = new Folder("root");

    public FileSystem() {
    }

    public void addFile(IFile file) throws FileNameExistsException {
        root.addFile(file);
    }
    public long findLargestFile(){
        return root.findLargestFile();
    }
    public void sortBySize(){
        root.sortBySize();
    }

    @Override
    public String toString() {
        return root.getFileInfo(0);
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