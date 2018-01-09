## CheckStyle-IDEA Plugin

This plugin contains two default configuration files for style checks.
- SUN
- Google

It is possible to enable another configuration but it is hard to find one which fits and is possible to compile.
(maybe this one: [checkstyle](https://github.com/checkstyle/checkstyle/blob/da2bf5965502515635eedea39211b50b05ef7cb0/config/checkstyle_checks.xml) or this one [picasso](https://github.com/square/picasso/blob/master/checkstyle.xml), [stackoverflow](https://stackoverflow.com/questions/39551492/checkstyle-how-to-disable-missing-package-info-java-file))

### SUN
Advantages | Disadventages
-|-
Errors, code is red | Errors, force to change them
less issues | 

### Google
Advantages | Disadventages
-|-
warnings, no force to change them | warnings, everybody can ignore different of them
_ | a lot of issues
_ | indents check

## Notes
ShoppingItem und ShoppingItemDAO angepasst
SUN:
Data - 41 E 8.37 - 8.48 --> what is 'verbirgt ein Feld'??
DAO - 51 E 16.16 - 16.31
Google:
Data - 40 W 16.32 - 16.36  // 36 because of indent
DAO - 172 W 16.36 - 16.40 // 148 because of indent

Fazit: Google indent anpassen auf 4 / 8, name DAO nicht erlaubt streichen, check einfügen final in method (zuerst vielleicht schauen ob prg noch läuft und was der vorteil sein soll), check for package-info einbauen, beschrieb return tag einbauen, javadoc für methoden und klassen required machen, jedoch getter und setter ignorieren

