#! /bin/sh

echo "--- REQUESTING CONTACT 2 IN XML--------"
curl -X GET \
     -H "Accept: application/xml" \
     -v "http://localhost:8080/contactsJPA/rest/contacts/3"
echo " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- REQUESTING CONTACT 1 IN JSON--------"
curl -X GET \
     -H "Accept: application/json" \
     -v "http://localhost:8080/contactsJPA/rest/contacts/4"
echo " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- REQUESTING CONTACT 22 --------"
curl -X GET \
     -H "Accept: application/xml" \
     -v "http://localhost:8080/contactsJPA/rest/contacts/22"
echo " "
echo "-------------------------------------------------------------------------------------------------"

echo "--- REQUESTING CONTACT with id NEW --------"
curl -X GET \
     -H "Accept: application/json" \
     -v "http://localhost:8080/contactsJPA/rest/contacts/NEW"
echo " "
echo "-------------------------------------------------------------------------------------------------"