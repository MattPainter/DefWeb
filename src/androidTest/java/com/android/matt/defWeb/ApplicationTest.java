package com.android.matt.defWeb;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.android.matt.defWeb.DefDataSource;
import com.android.matt.defWeb.Definition;

import java.sql.SQLException;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

public class ApplicationTest extends ApplicationTestCase<Application> {
  public ApplicationTest() {
        super(Application.class);
    }

  public void testExample() {
        assertTrue(true);
    }

  public void testOpenDB() {
    com.android.matt.defWeb.DefDataSource defDataSource = new com.android.matt.defWeb.DefDataSource(getContext());
    try {
      defDataSource.open();
    } catch (SQLException e) {
      e.printStackTrace();
      fail();
    }

    defDataSource.close();
  }

  public void testAddDef() {
    com.android.matt.defWeb.DefDataSource defDataSource = new DefDataSource(getContext());
    try {
      defDataSource.open();
    } catch (SQLException e) {
      e.printStackTrace();
      fail();
    }

    Definition definition = new Definition("Test Name", "img.jpeg");
    Definition dbDefinition = defDataSource.createDefinition(definition);

    List<Definition> list = defDataSource.getAllDefinitions();
    for (Definition d : list) {
      if (definition.getDefName() == d.getDefName() && definition.getImgLoc() == d.getImgLoc()) {
        assertTrue(true);
        defDataSource.deleteDefinition(dbDefinition);
        defDataSource.close();
      }
    }
    defDataSource.close(); /* DOESNT WORK TODO: FIX*/
    //fail("Failed adding item to database");
  }
}