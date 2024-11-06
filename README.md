# Fsav
**Fsav is a command in-line tool made for resolving directories and filepaths using the terminal for easy acess**
Written in Pure Java, JDK SE 22 recommended.
# How does Fsav work?
You can save filepaths or directories using fsav, after saving you can easiely resolve them again in whatever directory you are, providing high file availability.
# Commands
**--version** returns the current version.

**--help** opens the 'help.html' file which also provides documentation.

**--save <filepath/dir> <tag>** saves the given filepath or directory in the database file, a custom tag can be given but isnt necessary.

**--find <tag/extension>** searches the database for the given tag, once found it will return the filepath/dir or it you can say !<extension> to return multiple entries with the same extension.

**--remove <tag>** removes the given tag and its corosponding filepath/dir from the database.

**--entries <filter> <limit>** returns a formatted list of all existing database entries without filters, then there are 2 filters, 1. !abc (lists in alphabetical order) 2. png (this is an example, filters for extension), the limit argument limits how many entries should be displayed.

**--dump** outputs raw database data meant for debugging

# Important Notes
Tags in Fsav can only exist one at a time.

Fsav automatically does some things for you, these are:
- **Automatic removal of extinct filepaths/dir** (throws Warning aswell to inform user).
- **Readable Database Format** (makes it easy to debug potential issues or to manually add filepaths/dirs).
- **Accepts wildcards as input** (if you have a file named `cat.png`, you can easiely just use `fsav --save *.png`, make sure you dont parse multiple files as an input)
- **Existing Tag Checking** (when saving under a tag, fsav checks if the tag already exists)

# Example Usage (Win-Powershell) 
Here is an Example of how Fsav can be useful in finding and using files `imgprocess $(fsav --find catPNG) --large`, this will execute the command within the brackets first causing it to look like this: `imgprocess C:\Users\admin\cats\cat1.png --large` and it can also be used for getting multiple files like: `filemover "$(fsav --find projectDIR)/*.java"`, which returns the wildcard matches in the given directory.

# Download
Fsav is lightweight and written in Java (write once, run anywhere).
In order to install fsav onto your device, currently, you must configure it yourself to your liking, this may change in the future.
