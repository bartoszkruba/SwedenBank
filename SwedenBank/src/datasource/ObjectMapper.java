package datasource;

import datasource.annotations.Column;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectMapper<T> {

   private Class classToMap;
   private Map<String, Field> fields;

   public ObjectMapper(Class classToMap) {
      this.classToMap = classToMap;
      this.fields = new HashMap<>();

      Field[] fieldArr = classToMap.getDeclaredFields();

      for (Field field : fieldArr) {
         Column column = field.getAnnotation(Column.class);
         if (column != null) {
            String columnName = column.value().equals("") ? field.getName() : column.value();
            fields.put(columnName, field);
         }
      }
   }

   public T map(Map<String, Object> row) {
      try {
         T objectToMap = (T) classToMap.getConstructor().newInstance();

         for (Map.Entry e : row.entrySet()) {
            Object value = e.getValue();

            Field field = fields.get(e.getKey());

            if (field != null) {
               field.setAccessible(true);
               field.set(objectToMap, value);
            }
         }
         return objectToMap;
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public List<T> map(List<Map<String, Object>> rows) {
      List<T> objectsToMap = new ArrayList<>();

      for (Map<String, Object> row : rows) {
         T objectToMap = this.map(row);

         if (objectToMap != null) {
            objectsToMap.add(objectToMap);
         }
      }

      return objectsToMap;
   }

   public List<T> map(ResultSet results) {
      try {
         List<Map<String, Object>> rows = new ArrayList<>();

         ResultSetMetaData metaData = results.getMetaData();
         int count = metaData.getColumnCount();

         while (results.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= count; i++) {
               String columnName = metaData.getColumnName(i);
               Object value = results.getObject(i);
               row.put(columnName, value);
            }
            rows.add(row);
         }
         return this.map(rows);
      } catch (SQLException e) {
         return null;
      }
   }
}
