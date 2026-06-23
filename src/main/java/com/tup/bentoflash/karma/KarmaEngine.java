package com.tup.bentoflash.karma;

import org.springframework.stereotype.Service;

@Service
public class KarmaEngine {

    private static final int REWARD_POINTS   = 10;

    private static final int PENALTY_POINTS  = 25;

    private static final int MIN_SCORE       = 0;

    private static final int MAX_SCORE       = 200;

    public int calculateNewScore(int currentScore, boolean pickedUpOnTime) {

        int delta = pickedUpOnTime ? REWARD_POINTS : -PENALTY_POINTS;

        int newScore = currentScore + delta;

        if (newScore < MIN_SCORE) {
            return MIN_SCORE;
        } else if (newScore > MAX_SCORE) {
            return MAX_SCORE;
        }
        
        return newScore;
    }

    public boolean isScoreCritical(int score) {
        final int CRITICAL_THRESHOLD = 30;
        return score <= CRITICAL_THRESHOLD;
    }
}
