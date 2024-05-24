Examples for jasper report

1. To Test:
* Import project to your iDempiere Eclipse workspace.
* Duplicate service.product launch configuration.
* At the duplicated service.product launch configuration, goes to the plugin tab and select this plugin.
* Launch iDempiere using the duplicated service.product launch configuration.
* Included 2Pack:
  * **Jasper Balance Sheet Example** process, update the **Jasper Process** value of **Balance Sheet Current Month** financial report record.
  * **JasperOrder_Test** menu and process.

2. POM Files
* pom.xml
	* Use repository in the `<repositories>` section of POM for resolution of iDempiere core and target platform bundles.
	* Parent pom is ../parent-repository-pom.xml.

3. Folder structure for pom.xml 
```
workspace folder  
|  
+--- idempiere  
|  
|    +--- org.idempiere.parent  
|  
+--- idempiere-examples  
|  
|    +--- org.idempiere.jasper.example  
```
