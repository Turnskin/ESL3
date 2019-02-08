/* Pricer Server Software
 *
 * Confidential Property of Pricer AB (publ). Copyright © 1998-2018 Pricer AB (publ),
 * Box 215,Västra Järnvägsgatan 7, SE-101 24 Stockholm, Sweden. All rights reserved.
 */

import service.PublicAPI50Factory;
import ws.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static service.myTools.getFlashTime;
import static service.myTools.getPage2Flash;

// Don't forget to run mvn clean install so the WS plugin
// can generate the classes from the WSDL.

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String itemIPFarg = null;
        int timeToFlash = 0;
        try {
            //itemIPFarg = args[0];  // Чтение аргумента из коммандной строки
            itemIPFarg = getPage2Flash();
            timeToFlash = getFlashTime();
            if (itemIPFarg.equals("/?") || itemIPFarg.equals("?")) {
                System.out.println("The page name to flash NORMAL, DISCOUNT or your own used name.");
                System.out.println("The second parameter it is a flash time 30s, 60s, 1800s  ");
                System.out.println("В каталоге программы должен быть файл main.properties со следующими параметрами: ");
                System.out.println("// app.version=");
                System.out.println("// app.name=");
                System.out.println("username=");
                System.out.println("userAPIkey=");
                System.out.println("ip=");
                System.out.println("port=");
                System.out.println("flashtime=");
                System.out.println("Page2Flash=");
                return;
            }
        } catch (Exception e) {
            System.out.println("Не указана страница ценника для подсветки.");
            itemIPFarg = "";
            return;
        }

        try {
            if (api().isAlive(0)) {
//                System.out.println("Server is alive!");
            } else {
                System.out.println("Could not connect to the server...");
                System.out.println("API error: " + api().getLastError());
                return;
            }
        } catch (NullPointerException e) {
            System.out.println("Errors in the properties file");
            //System.out.println("API error: " + api().getLastError());
            return;
        }
        // Check service status
        // get system version

//        SystemVersion systemVersion = api().getSystemVersion();
//        System.out.println("Server version: " + systemVersion.getVersion());
        // get system version


        //System.out.println(CsvTextList);
        //System.out.println(api().getItems(CsvTextList));
        //Import from CSV(TXT) one item per row


//        List<ESL> linkedEsLs = api().getLinkedESLs("1235");
//        System.out.println("ESL " + linkedEsLs.get(0).getBarcode() + " is linked with item 1235.");

//        Iterator<String> EslListIterator = CsvTextList.iterator();
//        while (EslListIterator.hasNext()) {
//
//        }
        //System.out.println(linkedEsLs.toString());
        //api().flashLedOn("T4617300791813273", 30, true, true);
//        Item it = api().getItem("ЦР123456789");
//        System.out.println(it.getItemProperties());
//        System.out.println("Item: " + it);


//    Item item1 = api().getItem("1");
//
//    LinkInformation linkInformation = new LinkInformation();
//    api().link("ЦР123456780", "T4617300791813273", linkInformation);

//    Item item2 = new Item();
//    item2.setItemId("2");
//
//    PropertyValue pVal1 = new PropertyValue();
//    PropertyId pId1 = new PropertyId();
//    pId1.setNumber(7);
//    pVal1.setId(pId1);
//    pVal1.setValue("New item name");
//    item2.getItemProperties().add(pVal1);
//
//    PropertyValue pVal2 = new PropertyValue();
//    PropertyId pId2 = new PropertyId();
//    pId2.setNumber(23);
//    pVal2.setId(pId2);
//    pVal2.setValue("4.85");
//    item2.getItemProperties().add(pVal2);
//
//    List<Item> items = new ArrayList<>();
//    items.add(item2);
//
//    api().updateItems(items);
//
        Item test = new Item();
        List<ESL> linkedEsLs = null;
        List<String> CsvTextList = null;
        int ItemsCount = api().getTotalNumberOfItems();
        Limit LimitItemRange = new Limit();
        LimitItemRange.setNumberOfElements(ItemsCount);
        List<ItemESLLink> ttt = api().getAllLinks(LimitItemRange);
        List<String> AllLinkedItemsId = new ArrayList<String>();
        List<Item> AllLinkedItem = new ArrayList<Item>();
        String ItemId = new String();
        for (ItemESLLink iter : ttt) {
            ItemId = iter.getItemIdList().get(0).toString();
            AllLinkedItemsId.add(ItemId);
        }
        AllLinkedItem.addAll(api().getItems(AllLinkedItemsId));
        ArrayList<ESL> EslToFlash = new ArrayList<ESL>();
        List<String> AllDiscountItems = new ArrayList<String>();
        String itemID;
        String itemIPF = "";
        List<Item> AllItemProperties = new ArrayList<Item>();
        if (AllLinkedItem.isEmpty()) {
            System.out.println("There is no linked Items");
            return;
        }
        test = AllLinkedItem.get(0);
        int ITEMDPTIDREF =  0;
        int ITEMIPF = 0;
        int SizeOfPropertiesList = test.getItemProperties().size();
        for (int i = 0;i < SizeOfPropertiesList; i++) {
             if (test.getItemProperties().get(i).getId().getNumber().intValue() == 2) {
                 ITEMDPTIDREF = i;
             }
            if (test.getItemProperties().get(i).getId().getNumber().intValue() == 121) {
                ITEMIPF = i;
            }
        }
        for (Item iter : AllLinkedItem) {
            itemIPF = iter.getItemProperties().get(ITEMIPF).getValue();
            if (itemIPFarg.equals(itemIPF.toString())) {
                itemID = iter.getItemProperties().get(ITEMDPTIDREF).getValue();
                linkedEsLs = api().getLinkedESLs(itemID);
                EslToFlash.addAll(linkedEsLs);
                // System.out.println("itemID: " + iter.getItemProperties().get(ITEMDPTIDREF).getValue() + " ESL to FLASH: " + linkedEsLs.get(0).getBarcode());
            }
        }
        for (ESL iter : EslToFlash) {
            api().flashLedOn(iter.getBarcode(), timeToFlash, true, true);
        }
//        try {
//             CsvTextList = CsvToList("src/items.csv");
//
//            for (String rowFromCsv : CsvTextList) {
//                ASize = rowFromCsv.length();
//                ItemIdText = rowFromCsv.substring(1, ASize - 1);
//                linkedEsLs = api().getLinkedESLs(ItemIdText);
//                EslToFlash.addAll(linkedEsLs);
//            }
//
//            int spisokLenght = EslToFlash.size();
//            for (int temp = 0; temp < spisokLenght; temp++) {
//                System.out.println("ESL's Barcode to print " + EslToFlash.get(temp).getBarcode());
//                //api().flashLedOn(EslToFlash.get(temp).getBarcode(), 30, true, true);
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("The file  was not found.");
//        }
        //api().flashLedOn(linkedEsLs.get(0).getBarcode(), 30, true, true);
    }

    private static PricerPublicAPI50 api() throws FileNotFoundException {
            return PublicAPI50Factory.getConnection();
    }
}