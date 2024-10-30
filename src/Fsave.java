import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Fsave {
    public static Entry resolveEntry(String tag, Database database) {
        for(Entry dbEntry : database.getEntries()) {
            if(dbEntry.getTag().equals(tag)) {
                return dbEntry;
            }
        }
        return null;
    }

    public static List<Entry> resolveEntries(String extension, Database database) {
        List<Entry> matching = new ArrayList<>();
        for(Entry entry : database.getEntries()) {
            if(entry.getExtension().equals(extension)) {
                matching.add(entry);
            }
        }
        return matching;
   }

    public static String save(String path, Database database) {
        return save(path, "r", database, false);
    }

    public static String save(String path, String tag, Database database, boolean withTag) {
        try {
            File file = FileSystem.listFilesByPatternOrNames(path, 1).get(0);
            if(file.exists()) {
                String extension = "[DIR]";
                if(!file.isDirectory()) {
                    extension = file.getName().substring(file.getName().indexOf('.')+1);
                    if(!withTag) {
                        tag = file.getName().substring(0, file.getName().indexOf('.'));
                    }
                } else if(!withTag) {
                    tag = file.getName();
                }
                Entry entry = new Entry(tag, file.getAbsolutePath(), extension);
                if(resolveEntry(tag, database) == null) {
                    database.addEntry(entry);
                    database.update();
                    return "Sucessfully saved "+tag+" extension: "+extension+" path: "+path+"!";
                }
                return "Tag already exists.";
            }
            return "File does not Exist, path: "+path;
        } catch (Exception e) {
            return "Failed to save file with path: "+path;
        }
    }

    public static String find(String tag, Database database) {
        if(tag.startsWith("!")) {
            String output = "";
            List<Entry> entries = resolveEntries(tag.substring(1), database);
            for(Entry entry : entries) {
                output+=entry.getFilepath()+" && ";
            }
            return output.length() > 2 ? output.substring(0, output.length()-3) : output;
        } else {
            Entry entry = resolveEntry(tag, database);
            if(entry != null) {
                return entry.getFilepath();
            }
        }
        return "Tag does not exist.";
    }

    public static String entries(Database database) {
        String output = "\033[04m----DATABASE ENTRIES----\033[0m\n";
        int index = 0;
        output += "\033[01mROW"+"\t:\t"+"TAG"+"\t:\t"+"PATH"+" : "+"EXTENSION"+"\033[0m\n";
        for(Entry entry : database.getEntries()) {
            output += index+"\t:\t\033[93m"+entry.getTag()+"\033[0m\t:\t"+entry.getFilepath()+" : "+entry.getExtension()+"\n";
            ++index;
        }
        return output;
    }

    public static String entries(String filter, Database database) {
        return entries(filter, 9999999, database);
    }

    public static String entries(String filter, int limit, Database database) {
        String output = "\033[04m----DATABASE ENTRIES----\033[0m\n";
        int index = 0;
        int items = 0;
        output += "\033[01mROW"+"\t:\t"+"TAG"+"\t:\t"+"PATH"+" : "+"EXTENSION"+"\033[0m\n";
        List<Entry> enf = database.getEntries();
        if(filter.equals("!abc")) {
            Collections.sort(enf, new Comparator<Entry>() {
                @Override
                public int compare(Entry o1, Entry o2) {
                    return o1.getTag().compareToIgnoreCase(o2.getTag());
                }
            });
        }
        for(Entry entry : enf) {
            if((entry.getExtension().equals(filter) || filter.equals("!abc") || filter.equals("!default")) && items < limit) {
                output += index+"\t:\t\033[93m"+entry.getTag()+"\033[0m\t:\t"+entry.getFilepath()+" : "+entry.getExtension()+"\n";
                ++items;
            }
            ++index;
        }
        return output;
    }

    public static String remove(String tag, Database database) {
        Entry entry = resolveEntry(tag, database);
        if(entry != null) {
            database.getEntries().remove(entry);
            database.update();
            return "Sucessfully removed Entry "+tag+"!";
        }
        return "Entry "+tag+" does not exist.";
    }

    public static void main(String[] args) {
        if(args == null || args.length == 0) {
            System.out.println("Fatal Error: No arguments were given.");
            System.exit(0);
        }
        Database database = new Database();
        if(args[0].equals("--version")) {
            System.out.println("!--FileDirectorySaver--!\nMade by @Lithax\nCopyright Lithax All rights reserved\nVersion: 1.0");
        } else if(args[0].equals("--help")) {
            try {
                File htmlFile = new File("help.html");
                Desktop.getDesktop().browse(htmlFile.toURI());
            } catch (Exception e) {
                System.out.println("Error opening html.");
            }
        } else if(args[0].equals("--dump")) {
            System.out.println(database.getRaw());
        } else if(args[0].equals("--entries") && args.length-1 == 2) {
            System.out.println(entries(args[1], Integer.parseInt(args[2]), database));
        } else if(args[0].equals("--entries") && args.length-1 == 1) {
            System.out.println(entries(args[1], database));
        } else if(args[0].equals("--entries")) {
            System.out.println(entries(database));
        } else if(args[0].equals("--remove") && args.length-1 == 1) {
            System.out.println(remove(args[1], database));
        } else if(args[0].equals("--find") && args.length-1 == 1) {
            System.out.println(find(args[1], database));
        } else if(args[0].equals("--save") && args.length-1 == 1) {
            System.out.println(save(args[1], database));
        } else if(args[0].equals("--save") && args.length-1 == 2) {
            System.out.println(save(args[1], args[2], database, true));
        } else {
            System.out.println("Argument "+args[0]+" does not exist.");
        }
    }
}

class Entry {
    private String tag;
    private String filepath;
    private String extension;

    public Entry(String tag, String filepath, String extension) {
        this.tag = tag;
        this.filepath = filepath;
        this.extension = extension;
    }

    public String getTag() {
        return tag;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getExtension() {
        return extension;
    }
}

class Database {
    private String raw;
    private List<Entry> entries;
    
    public Database() {
        try {
            raw = read();
            entries = new ArrayList<>();
            entries = extract(raw);
            update();
        } catch (Exception e) {
            System.out.println("Fatal: Database could not be initialized.");
            raw = "";
            entries = new ArrayList<>();
        }
    }

    private String read() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\Program Files\\fsav\\fsav_db.fsv")));
            String line;
            String aw = "";
            while((line = reader.readLine()) != null) {
                aw+=line;
            }
            reader.close();
            return aw;
        } catch (Exception e) {
            System.out.println("Error reading the database.");
            return "";
        }
    }

    private void write(List<Entry> list) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\Program Files\\fsav\\fsav_db.fsv")))) {
            for (Entry entry : list) {
                if (entry != null) {
                    writer.write(entry.getTag() + "=\"" + entry.getFilepath() + "\"" + entry.getExtension() + "$\n");
                }
            }
        } catch (Exception e) {
            System.out.println("Error writing to database: " + e.getMessage());
        }
    }

    private List<Entry> extract(String rw) {
        int count = 0;
        List<Entry> list = new ArrayList<>();
        String[] st = rw.replaceAll("[\n]", "").split("[$]");
        for(String l : st) {
            ++count;
            String path = l.substring(l.indexOf('\"')+1, l.lastIndexOf('\"'));
            File file = new File(path);
            String tag = l.substring(0, l.indexOf('='));
            String extension = l.substring(l.lastIndexOf('\"')+1);
            if(file.exists()) {
                list.add(new Entry(tag, path, extension));
            } else {
                System.out.println("\033[0;33mWarning:\033[0m Entry "+count+" does not point to a file or directory. path: "+path+" tag: "+tag);
            }
        }
        return list;
    }

    public String getRaw() {
        return raw;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void removeEntry(int index) {
        entries.remove(index);
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public void update() {
        write(entries);
    }
}

class FileSystem {
    public static List<File> listFilesByPatternOrNames(String arg, int startIndex) throws Exception {
        List<File> sFiles = new ArrayList<>();
            if (arg.contains("*")) {
                Path path = Paths.get(arg).getParent();
                if (path == null) {
                    path = Paths.get(".");
                }
                String pattern = Paths.get(arg).getFileName().toString();
                File directory = path.toFile();
                
                File[] matchedFiles = matchFilesWithPattern(pattern, directory);
                for (File file : matchedFiles) {
                    sFiles.add(file);
                }
            } else {
                File file = new File(arg);
                if (file.exists()) {
                    sFiles.add(file);
                } else {
                    System.out.println("\033[0;33mWarning\033[0m: File " + arg + " not found.");
                }
            }
        return sFiles;
    }

    public static File[] matchFilesWithPattern(String pattern, File directory) {
        if (pattern.equals("*.*")) {
            return directory.listFiles(File::isFile);
        } else if (pattern.startsWith("*.")) {
            String extension = pattern.substring(2);
            return directory.listFiles((dir, name) -> name.endsWith("." + extension));
        } else if (pattern.endsWith(".*")) {
            String baseName = pattern.substring(0, pattern.length() - 2);
            return directory.listFiles((dir, name) -> name.startsWith(baseName + "."));
        } else if (pattern.contains("*")) {
            return directory.listFiles((dir, name) -> name.matches(pattern.replace("*", ".*")));
        } else {
            return new File[0];
        }
    }
}
