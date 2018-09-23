package com.example.vaibhav.baccalculator;

import java.util.ArrayList;
import java.util.HashMap;


public class BacStatusColor {
    private String ColorHexValue;
    private String StatusMessage;

    public String getColorHexValue() {
        return ColorHexValue;
    }

    public void setColorHexValue(String colorHexValue) {
        ColorHexValue = colorHexValue;
    }

    public String getStatusMessage() {
        return StatusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        StatusMessage = statusMessage;
    }

    public static HashMap<StatusColor,BacStatusColor> GetBacStatusColor()
    {
        return  new HashMap<StatusColor,BacStatusColor>(){
            {
                put(StatusColor.Amber,new BacStatusColor(){{ setStatusMessage("Be Careful..."); setColorHexValue("#FFC200"); }});
                put(StatusColor.Green,new BacStatusColor(){{ setStatusMessage("You're safe"); setColorHexValue("#3CB371"); }});
                put(StatusColor.Red,new BacStatusColor(){{ setStatusMessage("Over the limit!"); setColorHexValue("#B80028"); }});
            }
        };
    }
}
