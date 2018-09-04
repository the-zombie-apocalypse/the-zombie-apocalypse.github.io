package com.zorg.zombies.model.factory;

import com.zorg.zombies.model.MoveDirectionX;
import com.zorg.zombies.model.MoveDirectionY;
import com.zorg.zombies.model.MoveDirectionZ;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MoveDirectionFactoryTest {

    @InjectMocks
    private MoveDirectionFactory moveDirectionFactory = new MoveDirectionFactory();

    @Test
    void parseMoveDirection_When_ValidDirections_Expect_Parsed() {
        Arrays.asList(
                MoveDirectionX.WEST,
                MoveDirectionX.EAST,
                MoveDirectionY.NORTH,
                MoveDirectionY.SOUTH,
                MoveDirectionZ.DOWN,
                MoveDirectionZ.UP
        ).forEach(
                direction -> assertEquals(
                        direction,
                        moveDirectionFactory.parseMoveDirection(direction.name().toLowerCase())
                )
        );
    }

    @Test
    void parseMoveDirection_When_InvalidDirection_Expect_WrongMoveDirectionExceptionThrown() {
        assertThrows(WrongMoveDirectionException.class, () -> moveDirectionFactory.parseMoveDirection("wrong"));
    }
}