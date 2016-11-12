# PolyasUrn

Polya's Urn is an interesting mathematics conjecture which can be used to verify the results of an election using Bayes' Theorem. It is explained beautifully in [this video](https://youtu.be/ZM-i8t4pMK0) by Numberphile. This project is an implementation of Polya's Urn for experimentation and general fun.

## Parameters
* Candidate Percentage A/B: A general idea for what the split should be in the votes. This is not exactly what they will be--this is determined randomly--but it affords you *some* control over the results.
* Population and voter turnout: see the number of votes affects the spread
* Fraud: Skew the results even more with random fraudulent votes
* Iterations: Polya's Urn becomes more accurate wth more iterations (n=1000) is a good start
