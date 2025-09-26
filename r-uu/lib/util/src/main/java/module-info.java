module de.ruu.lib.util
{
    requires transitive jakarta.annotation;
    requires transitive jakarta.ws.rs;
    requires transitive org.slf4j;
    requires transitive lombok;
    requires transitive java.compiler;
    exports de.ruu.lib.util;
    exports de.ruu.lib.util.bimapped;
    exports de.ruu.lib.util.json;
    exports de.ruu.lib.util.lang.model;
    exports de.ruu.lib.util.classpath;
    // weitere exports nach Bedarf
}