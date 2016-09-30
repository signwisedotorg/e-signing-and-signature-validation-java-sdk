# javasdk
SignWise Services JAVA SDK
 
This is a Java SDK for using SignWise Services webservice. In order to use this first you need a keypair to
access the webservice. You can generate a keypair with openssl usign the following commands:

openssl genrsa -out private.key 2048

openssl req -new -key private.key -out request.csr

openssl x509 -req -days 3650 -in request.csr -signkey private.key -out cert.crt

now concatenate the key and certificate to one file and ceate a pkcs#12 keystore

cat cert.crt private.key > keys.pem

openssl pkcs12 -export -in keys.pem -out mykeystore.p12 -name "my keys"

Openssl will ask for a password for the newly created keystore.
You need to register your developer certificate. Send an email to support@signwiseservices.com and
ask for the signup document. This is a pdf file where you document your servers ip-address, paste your certificate
in pem form and fill in some other details. Then digitally sign this document and send back to support@signwiseservices.com.
Once your certificate has been registered check if your connection to SignWise services works using:

curl -v -s -k --cert mykeystore.p12:password https://signwise-services-access-location/version

Please replace here signwise-services-access-location with correct services url obtained from support.

In case of success you schould get an answer like:
{"name":"dms-api","version":"1.1.2"}

In case of failure you'll probably get HTTP 400 as an error.
Now rename the SignWise-sample.cfg in conf directory to SignWise.cfg and input SignWise services access location, your keystore filename, keystore type
and password. You can get service location from support@signwiseservices.com. The api supports both PKCS#12 and JKS keystores. You can convert a pkcs12 keystore to jks keystore using the command:

keytool -importkeystore -srckeystore mykeystore.p12 -srcstoretype PKCS12 -srcstorepass password -destkeystore keykeystore.jks -deststoretype JKS -deststorepass new-password

Now compile the sdk using maven

mvn clean install -DskipTests

There are a few shell scripts for UNIX systems in the scripts directory. Try using the script

./scripts/testversion.sh

Check the CLASSPATH definition in the scripts. It is assumed the mavn has downloaded the required dependent java libraries to your local repository.
This schould give you the same answer as the previous curl command if the config file syntax is correct.

# Using the SDK
First you have to obtain an instance of SignWiseConnection so you can send commands to SignWise Services. 
You can either create an instance of SignWiseConnection directly by passing the URL to service location as well as
client keystore and truststore parameters or by using the connection info from the config file as follows:

ConfigManager.init(path-and-file-name-of-config-file);

ConfigManager cfg = ConfigManager.instance();

SignWiseConnection conn = cfg.getSignWiseConnection();

# Reading service version info
The method SignWiseConnection.serviceVersion() retrieves service version info.

There are no input parameters.
Please see the testprogramm TestVersion.java. You can invoke it using the script

./scripts/testversion.sh

In case of success the API returns an instance of VersionInfo and in case of failure an instance of ErrorResponse.

# Parsing certificate info
The method SignWiseConnection.serviceCertificateParse(X509Certificate cert) sends certifcate to server and returns parsed certificate info.
The only input parameter is an X509Certificate. You can read the certificate from a file or any other input source using the SignWiseConnection.readCertFromStream(InputStream) method.
Please see the testprogramm TestCertificateParse.java. You can invoke it using the script

./scripts/testcertparse.sh path-and-file-name-of-a-certificate-in-base64

In case of success the API returns an instance of CertificateInfo and in case of failure an instance of ErrorResponse.

