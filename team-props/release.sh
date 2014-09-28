#!/bin/sh
set -e # On error just quit!

############################################################
#                                                          #
# Release script for the SQLiteProvider.                   #
#                                                          #
# Usage: release.sh [version] [/path/to/public-mvn-repo] #
#                                                          #
############################################################

GROUP_ID="com.novoda"
ARTIFACT_ID="sqliteprovider-core"
BINARY_FILE_PATH="core/target/sqliteprovider-core.jar"
PROJECT_NAME="SQLiteProvider"


if [ -z "$1" ]; then
    echo "No version provided"
    echo "Usage: release.sh [version] [/path/to/public-mvn-repo]"
    exit 1
fi
if [ -z "$2" ]; then
    echo "No path to public-mvn-repo provided"
    echo "Usage: release.sh [version] [/path/to/public-mvn-repo]"
    exit 1
fi


echo "You are about to release version $1 of $PROJECT_NAME to $2"
read -p "Are you sure you want to continue? (y/n) " response

if [[ $response =~ ^([yY][eE][sS]|[yY])$ ]]; then
    echo "Starting release"
else
    echo "Cancelled"
    exit 1
fi


ORIGINAL_WORKING_DIR=`pwd`

# Get the absolute path to this same script, which is the team-props directory
TEAM_PROPS_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# Make sure we run all these commands from the project root directory
cd "${TEAM_PROPS_DIR}/.."

echo "Building project"
mvn clean install
echo "Done"

echo "Deploying main binaries"
mvn deploy:deploy-file \
	-DgroupId="$GROUP_ID" \
	-DartifactId="$ARTIFACT_ID" \
	-Dversion="$1" \
	-Dpackaging=jar \
	-Dfile="$BINARY_FILE_PATH" \
	-Durl=file://"$2"/releases/
echo "Done"


echo "Releasing & pushing generated binaries to $2"
cd "$2"
# Make sure we're in master and we've got the latest before pushing to public-mvn-repo
git checkout master
git pull origin master
git add . --all
git commit -m "Publishes version $1 of $PROJECT_NAME"
git push origin master
echo "Done"


# Go back to the original working directory
cd "${ORIGINAL_WORKING_DIR}"
echo "Release of version $1 of $PROJECT_NAME has been completed!"