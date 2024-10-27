# Fsav
**Fsav is a command in-line tool made for resolving directories and filepaths using the terminal for easy acess**
Written in Pure Java, JDK SE 22 recommended.
# How does Fsav work?
You can save filepaths or directories using fsav, after saving you can easiely resolve them again in whatever directory you are, providing high file availability.
# Commands
**--version** returns the current version.

**--help** opens the 'help.html' file which also provides documentation.

**--save <filepath/dir> <tag>** saves the given filepath or directory in the database file, a custom tag can be given but isnt necessary.

**--find <tag>** searches the database for the given tag, once found it will return the filepath/dir.

**--remove <tag>** removes the given tag and its corosponding filepath/dir from the database.

**--entries <filter> <limit>** returns a formatted list of all existing database entries without filters, then there are 2 filters, 1. !abc (lists in alphabetical order) 2. png (this is an example, filters for extension), the limit argument limits how many entries should be displayed.

**--dump** outputs raw database data meant for debugging

# Important Notes
Tags in Fsav can only exist one at a time.

Fsav automatically does some things for you, these are:
- **Automatic removal of extinct filepaths/dir** (throws Warning aswell to inform user).
- **Readable Database Format** (makes it easy to debug potential issues or to manually add filepaths/dirs).
- **Accepts wildcards as input** (if you have a file named 'cat.png', you can easiely just use 'fsav --save *.png', make sure you dont parse multiple files as an input)
- **Existing Tag Checking** (when saving under a tag, fsav checks if the tag already exists)

# Download
Fsav is lightweight and written in Java (write once, run anywhere).
In order to install fsav onto your device, you have these options:
