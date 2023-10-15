Example to have plugin column callout executed after core column callouts.

To Test:
- Import project to your iDempiere Eclipse workspace.
- Duplicate service.product launch configuration.
- At the duplicated service.product launch configuration, goes to the plugin tab and select this plugin.
- Launch iDempiere using the duplicated service.product launch configuration.

Note: The project doesn't include a maven pom so mvn verify wouldn't work.