# Organizer-App_Java-swing

The Organizer-Application consists of 4 subprograms:  

| **Phone-Book**                                         | **Address-Book**                                           |
|--------------------------------------------------------|------------------------------------------------------------|
| ![Phone-Book_Frame.png](Images%2FPhone-Book_Frame.png) | ![Address-Book_Frame.png](Images%2FAddress-Book_Frame.png) |

| **Note-Book**                                        | **Scheduler (WIP)**                                  |
|------------------------------------------------------|------------------------------------------------------|
| ![Note-Book_Frame.png](Images%2FNote-Book_Frame.png) | ![Scheduler_Frame.png](Images%2FScheduler_Frame.png) |

The Application as a whole is one JFrame with multiple Tabs.

## Address-Book & Phone-Book
Booth are realized with a JTable inside a JScrollPanel. Since booth applications
are using the same Layout and functions, a class `ScrollTable` is implemented
which consists of the JTable inside the JScrollPanel, a search-bar implemented
via JTextField and a JComboBox.

Booth applications read from a text-file inside the `Files`folder.
Each row in this text files corresponds to one row of the table. 
Each entry of one row is separated by a comma (except for names).
e.g.: `Mia Schneider, 8904879`

### searching
This is implemented in a way, that the table will actualize itself as soon as
the user inputs something inside the search bar.

The JComboBox is used to specify which column will be searched!

The nested class `searchBarList` inside `ScrollTable` is used to react to inputs
made to the search-bar. As soon as a letter or number is inputted, the listener
will call `reactToSearchBar` which is a composition of the function `actualizeTable`
and `searchTable`.  
`actualizeTable` will draw the table again, based on the inputted ArrayList 
(each entry is one row inside the table).  
`searchTable` reads the pattern given to the search-bar and find the matching
rows (for the selected column).

### Update existing entries
By simply selecting a cell inside the table and write to it, the table will be
updated. This will also lead to an immediately save and is handled via the
`TableListener` as nested class inside `ScrollTable` by calling the save-function
of ÀddressBook`or `PhoneBook`respectively.

### adding / deleting
WIP