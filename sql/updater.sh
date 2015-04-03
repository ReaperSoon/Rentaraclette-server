#!/bin/sh
BASEDIR=$(dirname $0)
for file in `find $BASEDIR -iname *.sql`
do
echo 'Executing' $file'...';
`mysql -u root < $file`
done