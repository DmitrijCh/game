package com.dmitrijch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    private final TextureRegion[] walkFrames;
    private final int numberOfWalkFrames = 8;
    private final float walkFrameDuration = 0.1f;
    private float walkStateTime = 0;

    private final TextureRegion[] runFrames;
    private boolean isRunning = false;

    private final TextureRegion[] jumpFrames;
    private final int numberOfJumpFrames = 12;
    private final float jumpFrameDuration = 0.1f;
    private float jumpStateTime = 0;
    private boolean isJumping = false;
    private float jumpVelocity = 400;
    private float gravity = -800;

    private final TextureRegion[] attackFirstFrames;
    private final int numberOfAttackFirstFrames = 6;
    private final float attackFirstFrameDuration = 0.05f;
    private boolean isFirstAttacking = false;
    private float attackFirstStateTime = 0;
    private final float attackFirstCooldown = 0.3f;

    private final TextureRegion[] attackSecondFrames;
    private final int numberOfAttackSecondFrames = 4;
    private final float attackSecondFrameDuration = 0.1f;
    private boolean isSecondAttacking = false;
    private float attackSecondStateTime = 0;
    private final float attackSecondCooldown = 0.3f;

    private final TextureRegion[] attackThirdFrames;
    private final int numberOfAttackThirdFrames = 3;
    private float attackThirdFrameDuration = 0.1f;
    private boolean isThirdAttacking = false;
    private float attackThirdStateTime = 0;
    private final float attackThirdCooldown = 0.3f;

    private final TextureRegion[] blockFrames;
    private final int numberOfBlockFrames = 2;
    private final float blockFrameDuration = 1f;
    private boolean isBlocking = false;
    private float blockStateTime = 0;
    private final float blockCooldown = 2f;

    private float x = 100;
    private float y = 100;
    private boolean isMoving = false;
    private final float animationResetTime = 0.5f;

    public Player() {
        // Загрузка текстуры с кадрами ходьбы
        Texture texture = new Texture("walk.png");
        // Разделение текстуры на отдельные кадры
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / numberOfWalkFrames, texture.getHeight());
        walkFrames = new TextureRegion[numberOfWalkFrames * tmp[0].length];
        // Выравнивание кадров
        System.arraycopy(tmp[0], 0, walkFrames, 0, numberOfWalkFrames);

        Texture runTexture = new Texture("run.png");
        TextureRegion[][] runTmp = TextureRegion.split(runTexture, runTexture.getWidth() / numberOfWalkFrames, runTexture.getHeight());
        runFrames = new TextureRegion[numberOfWalkFrames * runTmp[0].length];
        System.arraycopy(runTmp[0], 0, runFrames, 0, numberOfWalkFrames);

        // Загрузка текстуры с кадрами прыжка
        Texture jumpTexture = new Texture("jump.png");
        TextureRegion[][] jumpTmp = TextureRegion.split(jumpTexture, jumpTexture.getWidth() / numberOfJumpFrames, jumpTexture.getHeight());
        jumpFrames = new TextureRegion[numberOfJumpFrames * jumpTmp[0].length];
        System.arraycopy(jumpTmp[0], 0, jumpFrames, 0, numberOfJumpFrames);

        // Загрузка текстуры с кадрами атаки
        Texture attackTexture = new Texture("attackFirst.png");
        TextureRegion[][] attackTmp = TextureRegion.split(attackTexture, attackTexture.getWidth() / numberOfAttackFirstFrames, attackTexture.getHeight());
        attackFirstFrames = new TextureRegion[numberOfAttackFirstFrames * attackTmp[0].length];
        System.arraycopy(attackTmp[0], 0, attackFirstFrames, 0, numberOfAttackFirstFrames);

        // Загрузка текстуры с кадрами второй атаки
        Texture attackSecondTexture = new Texture("attackSecond.png");
        TextureRegion[][] attackSecondTmp = TextureRegion.split(attackSecondTexture, attackSecondTexture.getWidth() / numberOfAttackSecondFrames, attackSecondTexture.getHeight());
        attackSecondFrames = new TextureRegion[numberOfAttackSecondFrames * attackSecondTmp[0].length];
        System.arraycopy(attackSecondTmp[0], 0, attackSecondFrames, 0, numberOfAttackSecondFrames);

        // Загрузка текстуры с кадрами третьей атаки
        Texture attackThirdTexture = new Texture("attackThird.png");
        TextureRegion[][] attackThirdTmp = TextureRegion.split(attackThirdTexture, attackThirdTexture.getWidth() / numberOfAttackThirdFrames, attackThirdTexture.getHeight());
        attackThirdFrames = new TextureRegion[numberOfAttackThirdFrames * attackThirdTmp[0].length];
        System.arraycopy(attackThirdTmp[0], 0, attackThirdFrames, 0, numberOfAttackThirdFrames);

        // Загрузка текстуры с кадрами блокировки
        Texture blockTexture = new Texture("block.png");
        TextureRegion[][] blockTmp = TextureRegion.split(blockTexture, blockTexture.getWidth() / numberOfBlockFrames, blockTexture.getHeight());
        blockFrames = new TextureRegion[numberOfBlockFrames * blockTmp[0].length];
        System.arraycopy(blockTmp[0], 0, blockFrames, 0, numberOfBlockFrames);
    }

    public void update(float deltaTime) {
        float speed = (float) 100;

        // Обработка движения при беге
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            speed = 200;
            isRunning = true;
        } else {
            isRunning = false;
        }

        // Обработка движения при ходьбе
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= speed * deltaTime;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += speed * deltaTime;
            isMoving = true;
        } else {
            isMoving = false;
        }

        walkStateTime += deltaTime;
        if (!isMoving || walkStateTime > animationResetTime) {
            walkStateTime = 0;
        }

        // Обработка прыжка
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isJumping) {
            isJumping = true;
            jumpStateTime = 0;
            jumpVelocity = 400;
        }

        if (isJumping) {
            jumpStateTime += deltaTime;
            y += jumpVelocity * deltaTime + 0.5f * gravity * deltaTime * deltaTime;
            jumpVelocity += gravity * deltaTime;

            if (y <= 100) {
                y = 100;
                isJumping = false;
                jumpVelocity = 0;
                jumpStateTime = 0;
            }
        }

        // Обработка первой атаки
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !isFirstAttacking) {
            isFirstAttacking = true;
            attackFirstStateTime = 0;
        }

        if (isFirstAttacking) {
            attackFirstStateTime += deltaTime;
            if (attackFirstStateTime > attackFirstCooldown) {
                isFirstAttacking = false;
            }
        }

        // Обработка второй атаки
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && !isSecondAttacking) {
            isSecondAttacking = true;
            attackSecondStateTime = 0;
        }

        if (isSecondAttacking) {
            attackSecondStateTime += deltaTime;
            if (attackSecondStateTime > attackSecondCooldown) {
                isSecondAttacking = false;
            }
        }

        // Обработка третьей атаки
        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE) && !isThirdAttacking) {
            isThirdAttacking = true;
            attackThirdStateTime = 0;
        }

        if (isThirdAttacking) {
            attackThirdStateTime += deltaTime;
            if (attackThirdStateTime > attackThirdCooldown) {
                isThirdAttacking = false;
            }
        }

        // Обработка блокировки
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            if (!isBlocking) {
                isBlocking = true;
                blockStateTime = 0;
            }
        } else {
            isBlocking = false;
        }

        if (isBlocking) {
            blockStateTime += deltaTime;
            if (blockStateTime > blockCooldown) {
                isBlocking = false;
            }
        }

        // Проверка на выход за пределы края экрана с учетом смещения
        float screenWidth = Gdx.graphics.getWidth();
        float playerWidth = getCurrentFrameWidth();
        float edgeOffset = -190;

        if (x < 0) {
            x = 0;
        } else if (x + playerWidth > screenWidth - edgeOffset) {
            x = screenWidth - playerWidth - edgeOffset;
        }
    }

    // Представляет ширину текущего кадра анимации ходьбы персонажа
    private float getCurrentFrameWidth() {
        return walkFrames[0].getRegionWidth();
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame;
        int frameIndex;

        if (isJumping) {
            int jumpFrameIndex = (int) (jumpStateTime / jumpFrameDuration) % jumpFrames.length;
            currentFrame = jumpFrames[jumpFrameIndex];
        } else if (isFirstAttacking) {
            int attackFrameIndex = (int) (attackFirstStateTime / attackFirstFrameDuration) % attackFirstFrames.length;
            currentFrame = attackFirstFrames[attackFrameIndex];
        } else if (isSecondAttacking) {
            int attackFrameIndex = (int) (attackSecondStateTime / attackSecondFrameDuration) % attackSecondFrames.length;
            currentFrame = attackSecondFrames[attackFrameIndex];
        } else if (isThirdAttacking) {
            int attackFrameIndex = (int) (attackThirdStateTime / attackThirdFrameDuration) % attackThirdFrames.length;
            currentFrame = attackThirdFrames[attackFrameIndex];
        } else if (isBlocking) {
            int blockFrameIndex = (int) (blockStateTime / blockFrameDuration) % blockFrames.length;
            currentFrame = blockFrames[blockFrameIndex];
        } else {
            if (isRunning) {
                frameIndex = (int) (walkStateTime / walkFrameDuration) % runFrames.length;
                currentFrame = runFrames[frameIndex];
            } else {
                frameIndex = (int) (walkStateTime / walkFrameDuration) % walkFrames.length;
                currentFrame = walkFrames[frameIndex];
            }
        }

        if (currentFrame != null) {
            batch.draw(currentFrame, x, y);
        }
    }
}