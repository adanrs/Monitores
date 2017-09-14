/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author adan-
 */
public class Table {

    int bytes;
    String name;
    int count;
    String owner;

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Table(int bytes, String name, int count, String owner) {
        this.bytes = bytes;
        this.name = name;
        this.count = count;
        this.owner = owner;
    }
    
    public Table(String name,String owner) {
        this.name = name;
        this.owner = owner;
    }
}
