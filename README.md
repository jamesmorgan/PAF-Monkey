
# PAF-Monkey

Created by James.E.Morgan http://www.morgan-design.com

A light weight and simple utility application which can load and parse normalised raw PAF files from the Post Office U.k

See: http://www2.royalmail.com/marketing-services/address-management-unit/address-data-products/postcode-address-file-paf

## Usage:

To run the application you can either simply use maven to package (mvn package) into a JAR file and invoke on the command line or can load the project and run through your favourite IDE.

Before running this application you will need to run all .sql files found in /src/main/sql/ in order to create the raw data tables needed to load and save any paf data.

## Arguments

Command line arguments are as follows:

### Mandatory Arguments

* -directory = The directory where the path files are located, all path files should be found in one directory.
* -host = The host of the DB you want to populate with the path data.
* -username = The username to connect to your given database.
* -password = The password for the given user to use when connecting to the given database.

e.g. -directory YourFullPathDir -host DbHost -username DbUser -password DbPassword

### Optional Arguments

* --verbose = Include this to enable more verbose logging and log every batch insert, default false
* -schema = The database name to use when populating paf data, default 'paf'
* -definitionDirectory = The directory where the definition files data are held, default 'src/main/resources/definitions'
* -db = Specify either mysql or mongo, this will attempt to load the PAF data in to either data store
* -port = override the default port for each data store, default for mysql = 3306, default for mongodb = 27017. If no port is specified the defaults are used accoridng to the db type specified. 

### Unused / deprecated Arguments

* -mode = Used to either SOURCE or UPDATE the paf file data, default 'SOURCE'. This is not yet fully supported and will be made optional at a later date.

### Logging ##

Two logs will be produced when you run this application. 

* paf.log - logs application output, see --verbose mode
* paf_error_log - logs all errors when happen

---------------------------------------

## Notices 

I cannot be held responsible for any damage or harm which may come from using this tool. All the code is open source and available. You can do what you like with it :)

## License

> MIT License (MIT)
> 
> Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
> 
> The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
> 
> THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/jamesmorgan/paf-monkey/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

