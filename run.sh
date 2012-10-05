export PORT=8086
export DATABASE_URL=postgres://mqfmaqrghhzadm:hOOrRFjM4uOEE7AnXioI2PMH7I@ec2-23-23-201-251.compute-1.amazonaws.com:5432/dfuf5a1mmja01b
java -cp ./target/DataHub-1.0-SNAPSHOT.jar:./target/dependency/* ia.datahub.ui.DataFeedServlet
