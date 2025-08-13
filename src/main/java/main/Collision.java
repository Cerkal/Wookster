package main;

import entity.Entity;
import entity.Entity.Entity_Type;
import objects.SuperObject;
import objects.projectiles.Projectile;
import tile.Tile;

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

        int tileNum1 = 0;
        int tileNum2 = 0;

        switch (entity.direction) {
            case UP:
                entityTopRow = (entityBoundaryTop - entity.speed)/Constants.TILE_SIZE;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
                break;
            case DOWN:
                entityBottomRow = (entityBoundaryBottom + entity.speed)/Constants.TILE_SIZE;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                break;
            case LEFT:
                entityLeftCol = (entityBoundaryLeft - entity.speed)/Constants.TILE_SIZE;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                break;
            case RIGHT:
                entityRightCol = (entityBoundaryRight + entity.speed)/Constants.TILE_SIZE;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                break;
        }

        if (this.gamePanel.debugMap) {
            Tile currentTile = this.gamePanel.tileManager.getTile(this.gamePanel.player.getLocation());
            currentTile.tileHighlight = true;
        }

        if (
            gamePanel.tileManager.tile[tileNum1].collision == true ||
            gamePanel.tileManager.tile[tileNum2].collision == true
        ){
            entity.collisionOn = true;
            changeDirection(entity);
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
                case UP:
                    entity.solidArea.y -= entity.speed;
                    break;
                case DOWN:
                    entity.solidArea.y += entity.speed;
                    break;
                case LEFT:
                    break;
                case RIGHT:
                    entity.solidArea.x += entity.speed;
                    break;
            }
            if (entity.solidArea.intersects(object.solidArea)) {
                if (object.collision) {
                    entity.collisionOn = true;
                }
                if (player && object.visibility) {
                    collisionObject = object;
                }
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
                // changeDirection(collisionEntity);
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
            case UP:
                entity.solidArea.y -= entity.speed;
                break;
            case DOWN:
                entity.solidArea.y += entity.speed;
                break;
            case LEFT:
                entity.solidArea.x -= entity.speed;
                break;
            case RIGHT:
                entity.solidArea.x += entity.speed;
                break;
        }
        if (
            !target.isDead &&
            entity != target &&
            entity.solidArea.intersects(target.solidArea)
        ){
            entity.collisionOn = true;
            collisionEntity = target;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        target.solidArea.x = target.solidAreaDefaultX;
        target.solidArea.y = target.solidAreaDefaultY;
        return collisionEntity;
    }

    public Entity projectileCollision(Projectile projectile) {
        Entity collisionEntity = null;
        for (Entity target : gamePanel.npcs) {
            collisionEntity = getProjectileEntity(projectile, target);
            if (collisionEntity != null) {
                return collisionEntity;
            }
        }
        return collisionEntity;
    }

    public Entity getProjectileEntity(Projectile projectile, Entity target) {
        Entity collisionEntity = null;
        projectile.solidArea.x = projectile.worldX + projectile.solidArea.x;
        projectile.solidArea.y = projectile.worldY + projectile.solidArea.y;
        target.solidArea.x = target.worldX + target.solidArea.x;
        target.solidArea.y = target.worldY + target.solidArea.y;
        if (
            projectile.solidArea.intersects(target.solidArea) &&
            !target.isDead
        ){
            projectile.collisionOn = true;
            collisionEntity = target;
        }
        projectile.solidArea.x = projectile.solidAreaDefaultX;
        projectile.solidArea.y = projectile.solidAreaDefaultY;
        target.solidArea.x = target.solidAreaDefaultX;
        target.solidArea.y = target.solidAreaDefaultY;
        return collisionEntity;
    }

    public void checkTileProjectile(Projectile projectile) {
        int projectileBoundaryLeft = projectile.worldX + projectile.solidArea.x;
        int projectileBoundaryRight = projectile.worldX + projectile.solidArea.x + projectile.solidArea.width;
        int projectileBoundaryTop = projectile.worldY + projectile.solidArea.y;
        int projectileBoundaryBottom = projectile.worldY + projectile.solidArea.y + projectile.solidArea.height;

        int projectileLeftCol = projectileBoundaryLeft/Constants.TILE_SIZE;
        int projectileRightCol = projectileBoundaryRight/Constants.TILE_SIZE;
        int projectileTopRow = projectileBoundaryTop/Constants.TILE_SIZE;
        int projectileBottomRow = projectileBoundaryBottom/Constants.TILE_SIZE;

        int tileNum1 = 0;
        int tileNum2 = 0;

        switch (projectile.direction) {
            case UP:
                projectileTopRow = (projectileBoundaryTop - projectile.speed)/Constants.TILE_SIZE;
                tileNum1 = gamePanel.tileManager.mapTileNum[projectileLeftCol][projectileTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[projectileRightCol][projectileTopRow];
                break;
            case DOWN:
                projectileBottomRow = (projectileBoundaryBottom + projectile.speed)/Constants.TILE_SIZE;
                tileNum1 = gamePanel.tileManager.mapTileNum[projectileLeftCol][projectileBottomRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[projectileRightCol][projectileBottomRow];
                break;
            case LEFT:
                projectileLeftCol = (projectileBoundaryLeft - projectile.speed)/Constants.TILE_SIZE;
                tileNum1 = gamePanel.tileManager.mapTileNum[projectileLeftCol][projectileTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[projectileLeftCol][projectileBottomRow];
                break;
            case RIGHT:
                projectileRightCol = (projectileBoundaryRight + projectile.speed)/Constants.TILE_SIZE;
                tileNum1 = gamePanel.tileManager.mapTileNum[projectileRightCol][projectileTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[projectileRightCol][projectileBottomRow];
                break;
        }
        if (
            gamePanel.tileManager.tile[tileNum1].projectileCollision == true ||
            gamePanel.tileManager.tile[tileNum2].projectileCollision == true
        ){
            projectile.collisionOn = true;
        }
    }

    private void changeDirection(Entity entity) {
        if (entity.entityType != Entity_Type.PLAYER) {
            entity.setAction();
        }
    }
}
