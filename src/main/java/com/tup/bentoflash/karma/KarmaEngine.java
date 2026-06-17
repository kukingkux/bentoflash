package com.tup.bentoflash.karma;

/**
 * KarmaEngine.java
 * Module 4 - Karma & Loyalty System
 *
 * Responsible for calculating updated karma scores based on user behavior.
 * All score mutations must pass through this engine to ensure consistent
 * bounds enforcement and business rule application.
 */
public class KarmaEngine {

    // -------------------------------------------------------------------------
    // Constants — tweak these values without touching any logic
    // -------------------------------------------------------------------------

    /** Points awarded when the user picks up their order on time. */
    private static final int REWARD_POINTS   = 10;

    /** Points deducted when the user ghosts / misses their pickup. */
    private static final int PENALTY_POINTS  = 25;

    /** A karma score can never fall below this floor. */
    private static final int MIN_SCORE       = 0;

    /** A karma score can never exceed this ceiling. */
    private static final int MAX_SCORE       = 200;

    // -------------------------------------------------------------------------
    // Core calculation — the method you own per the class diagram
    // -------------------------------------------------------------------------

    /**
     * Calculates the user's new karma score after a pickup event.
     *
     * <p>Rules:
     * <ul>
     *   <li>On-time pickup  → +{@value #REWARD_POINTS} points (loyalty reward)</li>
     *   <li>Missed pickup   → -{@value #PENALTY_POINTS} points (ghost penalty)</li>
     *   <li>Result is always clamped to [{@value #MIN_SCORE}, {@value #MAX_SCORE}]</li>
     * </ul>
     *
     * @param currentScore   the user's karma score before this event
     * @param pickedUpOnTime {@code true} if the user collected their order on time,
     *                       {@code false} if they ghosted the pickup
     * @return               the updated karma score, clamped within valid bounds
     */
    public int calculateNewScore(int currentScore, boolean pickedUpOnTime) {

        // 1. Apply the appropriate delta based on pickup behaviour
        int delta = pickedUpOnTime ? REWARD_POINTS : -PENALTY_POINTS;

        // 2. Compute raw new score
        int newScore = currentScore + delta;

        // 3. Clamp: never below MIN_SCORE, never above MAX_SCORE
        if (newScore < MIN_SCORE) {
            return MIN_SCORE;
        } else if (newScore > MAX_SCORE) {
            return MAX_SCORE;
        }
        
        return newScore;
    }

    // -------------------------------------------------------------------------
    // Optional helper — useful for service/controller layers
    // -------------------------------------------------------------------------

    /**
     * Convenience method that tells callers whether a score is in a "danger zone"
     * (i.e. the user is at risk of being suspended from the platform).
     *
     * @param score the karma score to evaluate
     * @return {@code true} if the score is critically low
     */
    public boolean isScoreCritical(int score) {
        final int CRITICAL_THRESHOLD = 30;
        return score <= CRITICAL_THRESHOLD;
    }
}
