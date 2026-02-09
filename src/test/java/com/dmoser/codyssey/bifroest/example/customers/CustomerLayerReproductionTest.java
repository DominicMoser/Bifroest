package com.dmoser.codyssey.bifroest.example.customers;

import static org.junit.jupiter.api.Assertions.*;

import com.dmoser.codyssey.bifroest.app.BifroestCliApp;
import com.dmoser.codyssey.bifroest.io.communications.SimpleConsoleCommunication;
import com.dmoser.codyssey.bifroest.structure.Layer;
import org.junit.jupiter.api.Test;

public class CustomerLayerReproductionTest {
  @Test
  public void testCustomerLayerCompletionTreeBuild() {
    CustomerLayer layer = new CustomerLayer();
    TestBifroestCliApp runner = new TestBifroestCliApp("test", layer);

    assertDoesNotThrow(() -> runner.triggerInit());
  }

  private static class TestBifroestCliApp extends BifroestCliApp {
    public TestBifroestCliApp(String name, Layer rootLayer) {
      super(name, rootLayer, new SimpleConsoleCommunication(), null, null);
    }

    public void triggerInit() {
      this.init();
    }
  }
}
