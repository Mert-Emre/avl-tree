To run the program first compile all files. Than call the command:
Java Main <input_file> <output_file>
input files are given, you should use them as input files, output file is arbitrary.

This project mainly implements and uses an avl tree. There is a mafia organization. It has one boss and every member of the organization has some kind of reputation score which is called gms. Every member has at most 2 lower ranking member to give orders. If there are two lower-ranking members under the command of a higher-ranking member,
one must have a higher GMS than the commander’s GMS, and one must have a lower GMS than the commander’s GMS.
The organization ensures that among the members that have no members to command, the highest ranking and the lowest ranking should not have more than 1 rank difference. Rank of the boss is 0 and it increases as we move down in the avl tree.
