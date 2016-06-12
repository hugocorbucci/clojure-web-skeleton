#!/usr/bin/env bash
set -e

if [[ $# -eq 0 ]]; then
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
if [[ ! -z "$2" ]]; then
  OLD_NAME=`echo "$2" | sed -e "s/_/-/g"`
fi
OLD_FOLDER_NAME=`echo "${OLD_NAME}" | sed -e "s/-/_/g"`

NEW_NAME=`echo "$1" | sed -e "s/_/-/g"`
NEW_NAME_HUMAN=`echo "$1" | sed -e "s/_/ /g" -e "s/-/ /g" | awk '{printf "%s%s", toupper(substr($0, 0, 1)), substr($0, 2)}'`
NEW_FOLDER_NAME=`echo "${NEW_NAME}" | sed -e "s/-/_/g"`

BACKUP_EXTENSION=.project-rename-backup

sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" src/clj/${OLD_FOLDER_NAME}/web.clj
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" src/clj/${OLD_FOLDER_NAME}/status.clj
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" src/clj/${OLD_FOLDER_NAME}/hello.clj
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" src/clj/${OLD_FOLDER_NAME}/db/config.clj
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" src/clj/${OLD_FOLDER_NAME}/db/migration.clj
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" test/clj/${OLD_FOLDER_NAME}/test_helper.clj
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" test/clj/${OLD_FOLDER_NAME}/sample_test.clj
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" src/gradle/${OLD_FOLDER_NAME}/listener.clj
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" src/gradle/${OLD_FOLDER_NAME}/servlet.clj
mv src/clj/${OLD_FOLDER_NAME} src/clj/${NEW_FOLDER_NAME}
mv src/gradle/${OLD_FOLDER_NAME} src/gradle/${NEW_FOLDER_NAME}
mv test/clj/${OLD_FOLDER_NAME} test/clj/${NEW_FOLDER_NAME}
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" build.gradle
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" dev.sh
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" Procfile
sed -i ${BACKUP_EXTENSION} -e "s/\/${OLD_FOLDER_NAME}\//\/${NEW_FOLDER_NAME}\//g" -e "s/${OLD_NAME}/${NEW_NAME}/g" -e "s/${OLD_NAME}/${NEW_NAME_HUMAN}/ig" project.clj
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" settings.gradle
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" gradle.properties
sed -i ${BACKUP_EXTENSION} -e "s/${OLD_NAME}/${NEW_NAME}/g" war/WEB-INF/web.xml
printf "# ${NEW_NAME}\n\nTODO: Describe project" > README.md
find . -name "*${BACKUP_EXTENSION}" | xargs rm -f
