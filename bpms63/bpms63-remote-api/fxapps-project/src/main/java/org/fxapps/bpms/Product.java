package org.fxapps.bpms;

/**
 * This class was automatically generated by the data modeler tool.
 */

public class Product implements java.io.Serializable
{

   static final long serialVersionUID = 1L;

   private java.lang.Integer id;
   private java.lang.String name;
   private java.lang.Float price;

   public Product()
   {
   }

   public java.lang.Integer getId()
   {
      return this.id;
   }

   public void setId(java.lang.Integer id)
   {
      this.id = id;
   }

   public java.lang.String getName()
   {
      return this.name;
   }

   public void setName(java.lang.String name)
   {
      this.name = name;
   }

   public java.lang.Float getPrice()
   {
      return this.price;
   }

   public void setPrice(java.lang.Float price)
   {
      this.price = price;
   }

   public Product(java.lang.Integer id, java.lang.String name,
         java.lang.Float price)
   {
      this.id = id;
      this.name = name;
      this.price = price;
   }
   
   public String toString() {
       return "[id=" + id + ", name=" + name + ", price=" + price + "]";
   }

}