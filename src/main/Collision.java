package main;

import entity.Entity;
import entity.Entity.Direction;
import entity.Entity.Entity_Type;
import objects.SuperObject;

public class Collision {

    GamePanel gamePanel;

    public Collision(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void checkTile(Entity entity) {
        int entityBoundaryLeft = entity.worldX + entity.solidArea.x;
        int entityBoundaryRight = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityBoundaryTop = entity.worldY + entity.solidArea.y;
        int entityBoundaryBottom = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityBoundaryLeft/Constants.TILE_SIZE;
        int entityRightCol = entityBoundaryRight/Constants.TILE_SIZE;
        int entityTopRow = entityBoundaryTop/Constants.TILE_SIZE;
        int entityBottomRow = entityBoundaryBottom/Constants.TILE_SIZE;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case Direction.UP:
                entityTopRow = (entityBoundaryTop - entity.speed)/Constants.TILE_SIZE;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
                if (
                    gamePanel.tileManager.tile[tileNum1].collision == true ||
                    gamePanel.tileManager.tile[tileNum2].collision == true
                ){
                    entity.collisionOn = true;
                    changeDirection(entity);
                }
                break;
            case Direction.DOWN:
                entityBottomRow = (entityBoundaryBottom + entity.speed)/Constants.TILE_SIZE;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (
                    gamePanel.tileManager.tile[tileNum1].collision == true ||
                    gamePanel.tileManager.tile[tileNum2].collision == true
                ){
                    entity.collisionOn = true;
                    changeDirection(entity);
                }
                break;
            case Direction.LEFT:
                entityLeftCol = (entityBoundaryLeft - entity.speed)/Constants.TILE_SIZE;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                if (
                    gamePanel.tileManager.tile[tileNum1].collision == true ||
                    gamePanel.tileManager.tile[tileNum2].collision == true
                ){
                    entity.collisionOn = true;
                    changeDirection(entity);
                }
                break;
            case Direction.RIGHT:
                entityRightCol = (entityBoundaryRight + entity.speed)/Constants.TILE_SIZE;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (
                    gamePanel.tileManager.tile[tileNum1].collision == true ||
                    gamePanel.tileManager.tile[tileNum2].collision == true
                ){
                    entity.collisionOn = true;
                    changeDirection(entity);
                }
                break;
        }
    }

    public SuperObject objectCollision(Entity entity, boolean player) {
        SuperObject collisionObject = null;

        for (SuperObject object : gamePanel.objects) {

            entity.solidArea.x = entity.worldX + entity.solidArea.x;
            entity.solidArea.y = entity.worldY + entity.solidArea.y;
            object.solidArea.x = object.worldX + object.solidArea.x;
            object.solidArea.y = object.worldY + object.solidArea.y;

            switch (entity.direction) {
                case Direction.UP:
                    entity.solidArea.y -= entity.speed;
                    if (entity.solidArea.intersects(object.solidArea)) {
                        if (object.collision) {
                            entity.collisionOn = true;
                        }
                        if (player && object.visibility) {
                            collisionObject = object;
                        }
                    }
                    break;
                case Direction.DOWN:
                    entity.solidArea.y += entity.speed;
                    if (entity.solidArea.intersects(object.solidArea)) {
                        if (object.collision) {
                            entity.collisionOn = true;
                        }
                        if (player && object.visibility) {
                            collisionObject = object;
                        }
                    }
                    break;
                case Direction.LEFT:
                    entity.solidArea.x -= entity.speed;
                    if (entity.solidArea.intersects(object.solidArea)) {
                        if (object.collision) {
                            entity.collisionOn = true;
                        }
                        if (player && object.visibility) {
                            collisionObject = object;
                        }
                    }
                    break;
                case Direction.RIGHT:
                    entity.solidArea.x += entity.speed;
                    if (entity.solidArea.intersects(object.solidArea)) {
                        if (object.collision) {
                            entity.collisionOn = true;
                        }
                        if (player && object.visibility) {
                            collisionObject = object;
                        }
                    }
                    break;
                default:
                    break;
            }
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            object.solidArea.x = object.solidAreaDefaultX;
            object.solidArea.y = object.solidAreaDefaultY;
        }
        return collisionObject;
    }

    public Entity entityCollision(Entity entity) {
        Entity collisionEntity = null;
        for (Entity target : gamePanel.npcs) {
            collisionEntity = getCollidEntity(entity, target);
            if (collisionEntity != null) {
                return collisionEntity;
            }
        }
        return collisionEntity;
    }

    public Entity getCollidEntity(Entity entity, Entity target) {
        Entity collisionEntity = null;

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        target.solidArea.x = target.worldX + target.solidArea.x;
        target.solidArea.y = target.worldY + target.solidArea.y;

        switch (entity.direction) {
            case Direction.UP:
                entity.solidArea.y -= entity.speed;
                if (entity.solidArea.intersects(target.solidArea)) {
                    entity.collisionOn = true;
                    collisionEntity = target;
                }
                break;
            case Direction.DOWN:
                entity.solidArea.y += entity.speed;
                if (entity.solidArea.intersects(target.solidArea)) {
                    entity.collisionOn = true;
                    collisionEntity = target;
                }
                break;
            case Direction.LEFT:
                entity.solidArea.x -= entity.speed;
                if (entity.solidArea.intersects(target.solidArea)) {
                    entity.collisionOn = true;
                    collisionEntity = target;
                }
                break;
            case Direction.RIGHT:
                entity.solidArea.x += entity.speed;
                if (entity.solidArea.intersects(target.solidArea)) {
                    entity.collisionOn = true;
                    collisionEntity = target;
                }
                break;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        target.solidArea.x = target.solidAreaDefaultX;
        target.solidArea.y = target.solidAreaDefaultY;
        return collisionEntity;
    }

    private void changeDirection(Entity entity) {
        if (entity.entityType != Entity_Type.PLAYER) {
            entity.attemptedDirections.remove(entity.direction);
            entity.setAction();
        }
    }
}
