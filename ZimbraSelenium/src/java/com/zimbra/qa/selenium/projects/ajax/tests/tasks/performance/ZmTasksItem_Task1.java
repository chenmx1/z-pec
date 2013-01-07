package com.zimbra.qa.selenium.projects.ajax.tests.tasks.performance;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.framework.util.performance.PerfKey;
import com.zimbra.qa.selenium.framework.util.performance.PerfMetrics;
import com.zimbra.qa.selenium.framework.util.performance.PerfToken;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class ZmTasksItem_Task1 extends AjaxCommonTest{

   public ZmTasksItem_Task1() {
      logger.info("New " + ZmTasksItem_Task1.class.getCanonicalName());

      // All tests start at the login page
      super.startingPage = app.zPageTasks;

      // Make sure we are using an account with message view
      super.startingAccountPreferences = null;
   }

   @Test(description="Measure the time to view a task",
         groups={"performance"})
   public void ZmTasksItem_01() throws HarnessException {
      String subject1 = "task1"+ ZimbraSeleniumProperties.getUniqueString();
      String subject2 = "task2"+ ZimbraSeleniumProperties.getUniqueString();

      // Create 2 tasks because by default when the latest one on the list
      // will be selected, thus selecting the first one to measure the performance
      app.zGetActiveAccount().soapSend(
            "<CreateTaskRequest xmlns='urn:zimbraMail'>" +
            "<m >" +
            "<inv>" +
            "<comp name='"+ subject1 +"'>" +
            "<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
            "</comp>" +
            "</inv>" +
            "<su>"+ subject1 +"</su>" +
            "<mp ct='text/plain'>" +
            "<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
            "</mp>" +
            "</m>" +
      "</CreateTaskRequest>");

      FolderItem taskFolder = FolderItem.importFromSOAP(app.zGetActiveAccount(),
            SystemFolder.Tasks);

      // Create the second item
      app.zGetActiveAccount().soapSend(
            "<CreateTaskRequest xmlns='urn:zimbraMail'>" +
            "<m >" +
            "<inv>" +
            "<comp name='"+ subject2 +"'>" +
            "<or a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
            "</comp>" +
            "</inv>" +
            "<su>"+ subject2 +"</su>" +
            "<mp ct='text/plain'>" +
            "<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
            "</mp>" +
            "</m>" +
      "</CreateTaskRequest>");

      // Refresh the tasks view
      app.zTreeTasks.zTreeItem(Action.A_LEFTCLICK, taskFolder);

      PerfToken token = PerfMetrics.startTimestamp(PerfKey.ZmTaskItem,
            "Load the Task item");

      // Select the item
      app.zPageTasks.zListItem(Action.A_LEFTCLICK, subject1);

      PerfMetrics.waitTimestamp(token);
   }
}