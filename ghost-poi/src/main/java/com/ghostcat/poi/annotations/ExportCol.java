package com.ghostcat.poi.annotations;

import java.lang.annotation.*;

/**
 * @author AssGhost
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface ExportCol {
    String value();
}
