package com.mactso.hardernaturalhealing.common.logic;

import java.lang.reflect.Field;

import net.minecraft.world.food.FoodData;

/*
 * Common Code "Modloader Independent" Java Reflection Adapters for Minecraft Private Variables
 */
public class ReflectionAdapters {
	
	public static volatile Field tickTimerField = null;
	private static volatile Field exhaustionLevelField = null;
	
	public static boolean isTickTimerFieldNull(){
		if (tickTimerField == null)
			return true;
		return false;
	}
	
	public static boolean isExhaustionLevelFieldNull(){
		if (exhaustionLevelField == null)
			return true;
		return false;
	}
	
	/**
     * Lazily give access to the FoodData.tickTimer field via reflection.
     * Throws IllegalStateException if the field is unreachable.
     */
	
    public static void initTickTimerField() {
        Field f = tickTimerField;
        if (f != null) return;

        try {
            f = FoodData.class.getDeclaredField("tickTimer");
            f.setAccessible(true);
            tickTimerField = f;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                "HarderNaturalHealing: Failed to reflect FoodData.tickTimer. Mod cannot function.", e
            );
        }
    }
    

/*
* Gets the private FoodData.exhaustionlevel value via reflection 
*/
public static int getTickTimerField(FoodData fs) {
    initTickTimerField();
    try {
        return tickTimerField.getInt(fs);
    } catch (IllegalAccessException e) {
        throw new IllegalStateException(
            "HarderNaturalHealing: Unable to get FoodData.tickTimerField", e
        );
    }
}
/*
* Sets the private FoodData.exhaustionlevel value via reflection 
*/
public static void setTickTimerField(FoodData fs, int value) {
    initTickTimerField();
    try {
    	tickTimerField.setInt(fs, value);
    } catch (IllegalAccessException e) {
        throw new IllegalStateException(
            "HarderNaturalHealing: Unable to set FoodData.tickTimerField", e
        );
    }
}

	/**
     * Lazily give access to the FoodData.exhaustionLevel field via reflection.
     * Throws IllegalStateException if the field is unreachable.
     */

    public static void initExhaustionLevelField() {
        Field f = exhaustionLevelField;
        if (f != null) return;

        try {
            f = FoodData.class.getDeclaredField("exhaustionLevel");
            f.setAccessible(true);
            exhaustionLevelField = f;
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                "HarderNaturalHealing: Failed to reflect FoodData.exhaustionLevel. Mod cannot function.", e
            );
        }
    }
    /*
    * Gets the private FoodData.exhaustionlevel value via reflection 
    */
    public static float getExhaustionLevel(FoodData fs) {
        initExhaustionLevelField();
        try {
            return exhaustionLevelField.getFloat(fs);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(
                "HarderNaturalHealing: Unable to get FoodData.exhaustionLevel", e
            );
        }
    }
    /*
    * Sets the private FoodData.exhaustionlevel value via reflection 
    */
    public static void setExhaustionLevel(FoodData fs, float value) {
        initExhaustionLevelField();
        try {
            exhaustionLevelField.setFloat(fs, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(
                "HarderNaturalHealing: Unable to set FoodData.exhaustionLevel", e
            );
        }
    }

}
