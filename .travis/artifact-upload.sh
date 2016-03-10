#! /bin/bash
 
local_file="$(ls $TRAVIS_BUILD_DIR/target/final/Parabot-V*.jar | head -n 1)"
local_path="$(ls $TRAVIS_BUILD_DIR/target/final/Parabot-V*.jar | head -n 1 | xargs -n 1 basename)"
target_url='ftp://$FTP_HOST/~/public_html/uploads/version_control/$local_file'
 
echo "Uploading $local_file to $target_url"
curl -u $FTP_USERNAME:$FTP_PASSWORD -T "$local_path" "$target_url"