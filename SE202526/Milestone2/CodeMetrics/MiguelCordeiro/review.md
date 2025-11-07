## Review

# Change Log
- 7/11/25 - Tomás Silva

# Lorenz-Kidd Metrics Set
- The analysis successfully applies the Lorenz-Kidd Metrics (NOO, NOOM, NOA) to the codebase. The metrics are clearly 
explained and are well-related to the potential problems and code smells they can indicate (e.g., high NOO/NOA linked
to God Class; high NOOM linked to Refused Bequest).
  
- However, the report currently lacks the quantitative context required to turn these observations into objective 
conclusions. The qualitative interpretation is correct, but the report needs to be grounded in quantifiable, predefined 
thresholds to convert observations into actionable engineering conclusions.

- To significantly improve the analysis, you must introduce concrete ranges or thresholds for each metric. What are the 
defined limits for NOO, NOA, and NOOM that classify a value as "Good," "Medium (Monitored)," or "Bad (Problematic)"? 
Without these ranges, stating that a value (like NOO of 468) is "very high" is subjective.

- Finally, it would be beneficial if the class names within the chart images were made more perceptible to enhance overall clarity.

