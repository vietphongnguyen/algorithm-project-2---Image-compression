Start and run “ImageCompressionSVD” and giving it the arguments from the command line. There are 4 types of arguments you could parse in as bellow: 

{1}  {pgm text image file name}
	Example: “1 image.pgm”, where “image.pgm” is a PGM image files in plain text format that you could easily open and edit in any text reader application like notepad on Window. This program will convert its format into a binary PGM image file. The output binary file name will add “_b” at the end. Thus, “image.pgm” will output to “image_b.pgm”.

{2} {pgm binary image file name} 
	Example: “2 image_b.pgm”. This program will revert the process which has been done in option one. It is going to convert this binary image file into a text image file. The output file name will remove the “_b” at the end. In case it does not end with “_b” the prefix “_t” will be added at the end of the file name. For example, “image_b.pgm” will output to “image.pgm”, “image.pgm” will output to “image_t.pgm”, and so on.

{3} [header text file name] [SVD text file name] [k integer value]
	Example: “3 header.txt SVD.txt 5”. The program will read the content of the header file to get the height (M), the width (N), and the grey scale level of the image. It will read the content of the SVD text file to get the unitary matrix (UMatrix), the rectangular diagonal value (RD), and the V matrix, and then write all the values into one binary file format named “image_b.pgm.SVD”. All three parameters [header text file name], [SVD text file name], and [k integer value] are optional. If they are missing then the default values will be used as: “header.txt”, “SVD.txt”, and M value will be use for “k”.

{4} [binary SVD file name of a PGM image]
	Example: “4 image_b.pgm.SVD”.  The program will read all of these values: the height (M), the width (N), the grey scale level, the unitary matrix (UMatrix), the rectangular diagonal value (RD), and the V matrix to reconstruct the PGM text image named “image_k.pgm”. The parameter [binary SVD file name of a PGM image] is optional, since if it is missing then the default values will be used as: “image_b.pgm.SVD”.
