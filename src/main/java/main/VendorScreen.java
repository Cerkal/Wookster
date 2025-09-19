package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import main.ScreenSelector.SelectionItem;
import main.ScreenSelector.SelectionResult;

public class VendorScreen {

    GamePanel gamePanel;
    List<InventoryItem> vendorInventory = new ArrayList<>();
    Entity entity;

    int selectedIndex;

    ScreenSelector screenSelector;
    
    public VendorScreen(GamePanel gamePanel, Entity entity) {
        this.gamePanel = gamePanel;
        this.entity = entity;

        screenSelector = gamePanel.ui.screenSelector;

        screenSelector.clearScreens();
        screenSelector.addScreen(getSelectionItems(this.gamePanel.player.getInventoryItemsForSale()));
        screenSelector.addScreen(getSelectionItems(entity.getInventoryItemsForSale()));
        screenSelector.setScreen(0);
    }

    public void drawScreens(Graphics2D graphics2D) {
        drawBoxes(graphics2D);

        screenSelector.set(0, getSelectionItems(this.gamePanel.player.getInventoryItemsForSale()));
        screenSelector.set(1, getSelectionItems(entity.getInventoryItemsForSale()));

        int active = screenSelector.getScreenIndex();
        int passive = active == 0 ? 1 : 0;

        int leftSideX = Constants.TILE_SIZE * 2 - 10;
        int leftSideY = Constants.TILE_SIZE * 4;

        graphics2D.setColor(Color.WHITE);
        int side = (int) (Constants.TILE_SIZE * 2.5);
        graphics2D.drawString(String.valueOf(this.gamePanel.player.getCredits()), Constants.SCREEN_WIDTH / 2 - side, Constants.TILE_SIZE * 2);

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(String.valueOf(entity.getCredits()), Constants.SCREEN_WIDTH - side, Constants.TILE_SIZE * 2);

        int rightSideX = Constants.SCREEN_WIDTH / 2 + (Constants.TILE_SIZE * 2) - 10;
        int rightSideY = Constants.TILE_SIZE * 4;

        SelectionResult selectedItem = screenSelector.selector(
            graphics2D,
            active == 0 ? leftSideX : rightSideX,
            active == 0 ? leftSideY : rightSideY,
            Constants.NEW_LINE_SIZE,
            false
        );

        screenSelector.display(
            graphics2D,
            active == 0 ? rightSideX : leftSideX,
            active == 0 ? rightSideY : leftSideY,
            Constants.NEW_LINE_SIZE,
            false,
            passive
        );

        // Handle Inventory Vending
        if (selectedItem.selected && selectedItem.customKeyPress == -1) {
            InventoryItem inventoryItem = (InventoryItem) selectedItem.selectedObject;

            if (inventoryItem == null) return;

            if (selectedItem.selectedScreenIndex == Constants.VENDOR_PLAYER_INDEX) {
                // Selling to vendor
                if (entity.getCredits() - inventoryItem.price < 0) { return; }
                if (this.gamePanel.player.removeInventoryItemFromVendor(inventoryItem.name, 1)) {
                    InventoryItem singleItem = new InventoryItem(inventoryItem);
                    singleItem.count = 1;
                    singleItem.sellable = true;
                    this.gamePanel.player.addCredits(inventoryItem.price);
                    entity.removeCredits(inventoryItem.price);
                    entity.addInventoryItemFromVendor(singleItem);
                }
            } else {
                // Buying from vendor
                if (this.gamePanel.player.getCredits() - inventoryItem.price < 0) { return; }
                if (entity.removeInventoryItemFromVendor(inventoryItem.name, 1)) {
                    InventoryItem singleItem = new InventoryItem(inventoryItem);
                    singleItem.count = 1;
                    singleItem.sellable = true;
                    entity.addCredits(inventoryItem.price);
                    this.gamePanel.player.removeCredits(inventoryItem.price);
                    this.gamePanel.player.addInventoryItemFromVendor(singleItem);
                }
            }
            screenSelector.clearSelectionLeaveHighlight();
        }
    }

    private void drawBoxes(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLACK);

        int halfScreen = Constants.SCREEN_WIDTH / 2;
        int width = halfScreen - Constants.TILE_SIZE * 2;
        int height = Constants.SCREEN_HEIGHT - Constants.TILE_SIZE * 2;
        graphics2D.fillRect(Constants.TILE_SIZE, Constants.TILE_SIZE, width, height);

        graphics2D.setColor(Color.WHITE);
        graphics2D.setStroke(new BasicStroke(5));

        if (screenSelector.getScreenIndex() == 0) {
            graphics2D.drawRect(Constants.TILE_SIZE, Constants.TILE_SIZE, width, height);
        } else {
            graphics2D.drawRect(halfScreen + Constants.TILE_SIZE, Constants.TILE_SIZE, width, height);
        }
        
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(Constants.INVENTORY.toUpperCase(), Constants.TILE_SIZE * 2, Constants.TILE_SIZE * 2);

        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(halfScreen + Constants.TILE_SIZE, Constants.TILE_SIZE, width, height);

        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(entity.name.toUpperCase(), halfScreen + Constants.TILE_SIZE * 2, Constants.TILE_SIZE * 2);
    }

    private List<SelectionItem> getSelectionItems(List<InventoryItem> itemList) {
        List<SelectionItem> inventory = new ArrayList<>();
        for (InventoryItem item : new ArrayList<>(itemList)) {
            int maxCount = 25;
            String displayName = item.name;
            if (item.count > 1) displayName = item.name + " (" + item.count + ")";
            int diff = maxCount - displayName.length();
            displayName += " ".repeat(diff) + item.price;
            SelectionItem selectionItem = new SelectionItem(displayName, item);
            inventory.add(selectionItem);
        }
        return inventory;
    }
}
