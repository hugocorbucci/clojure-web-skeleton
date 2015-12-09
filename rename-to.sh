#!/usr/bin/env bash
set -e
set -o

if [ $# -eq 0 ]; then
  echo "Usage: $0 <new-project-name> [<old-name>]"
  echo ""
  echo "Required:"
  echo "new-project-name: The name to be used for your folders, packages, wars and namespaces."
  echo ""
  echo "Optional:"
  echo "old-name: The current name. Defaults to 'skeleton'. That's the string that will be replaced in files and paths."
  exit 1
fi

OLD_NAME="skeleton"
if [ ! -z "$2" ]; then
  OLD_NAME=`echo "$2" | sed -e "s/_/-/"`
fi
OLD_FOLDER_NAME=`echo "${OLD_NAME}" | sed -e "s/-/_/"`


NEW_NAME=`echo "$1" | sed -e "s/_/-/"`
NEW_FOLDER_NAME=`echo "${NEW_NAME}" | sed -e "s/-/_/"`

BACKUP_EXTENSION=.project-rename-backup

sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/" src/clj/${OLD_NAME}/web.clj
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/" test/clj/${OLD_NAME}/test_helper.clj
mv src/clj/${OLD_FOLDER_NAME} src/clj/${NEW_FOLDER_NAME}
mv test/clj/${OLD_FOLDER_NAME} test/clj/${NEW_FOLDER_NAME}
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/" build.gradle
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/" dev.sh
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/" Procfile
sed -i ${BACKUP_EXTENSION} -e "s/\/${OLD_FOLDER_NAME}\//\/${NEW_FOLDER_NAME}\//" -e "s/${OLD_NAME}/${NEW_NAME}/" project.clj
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/" settings.gradle
echo "# ${NEW_NAME}\n\nTODO: Describe project" > README.md
find . -name "*${BACKUP_EXTENSION}" | xargs rm -f
