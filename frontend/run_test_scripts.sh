rm -rf test-report.xml
npm run unit
cp test/unit/TESTS*.xml test-report.xml
rm test/unit/TESTS*.xml
