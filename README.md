HTM
===

An attempt at implementing the Numenta HTM model in java.

This is still very much a hobby/hacking project. I've gotten
some functionality to work with an unprovided image data
set.  Eventually I'd like to flush out the core components
and provide a highly parallelized implementation.

Hack away!

1/14/2015 - Recent updates:
* Created a gradle core project with subprojects:
* | core - base functionality including input channels and messaging from mmesh
* | sdr - core sparse, distributed concepts (original htm code)
* | memory - core temporal memory concepts (original mmesh code)
* Copied prototype code from the "mmesh" project to a "memory" subproject
* Moved a lot of code around, mostly down to "core"
* The original htm code now resides in the "sdr" subproject

Near-term hopeful changes:
* Refactor sdr to use the core's Identifier and messaging concepts

------------------------------
5/26/20 - UPDATE:
* The project has shifted a bit. What was a Java implementation of Numenta's HTM has slowly morphed into a homebrew AGI project.
* This is still a hacking project, I have a lot of code that's still local because much of it is old and needs refactoring.
* I'd probably recommend finding one of the Java Numenta HTM projects, consider this a general AI "playground".
* The goal is clear, however, AGI without limits, constraints and implementation worry. If AGI saves us for damns us, we probably deserve either outcome.
