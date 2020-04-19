# code-gladiator
This repository has the code to solve the following problems. Please read the problem(s) before going into the solution.
<ol>
  <li>
    <a href="https://www.techgig.com/practice/question/Y0Y2NndVR2FYTUFxZElLRkhRYzdPQT09"> Earthquake in Africa.</a>
  </li>
</ol>

<h1>Earthquake in Africa</h1>
<p>Africa has been hit by an earthquake. There are total N cities in Africa out of which K have been affected by this earthquake. The cities are numbered from 1 to N.</p>

<p>United Nations has decided to establish a relief base in one of these (N - K) non-affected cities. Although the UN has lot of relief packets, but it has a single carrier truck to distribute these packets. The truck starts at the base in the morning and goes to all the K cities one by one. It finally returns to the base again in the evening after distributing relief packets to all the K cities.</p>

<p>Note that the truck may have to visit some non-affected cities also while going from one affected city to some other affected city. There are M bi-directional roads between the cities.</p>

<img src="https://www.techgig.com/files/nicUploads/962627561885314.jpg"></img>
<span>Diagram corresponding to the sample input 1</span>

<p>It is guaranteed that all the cities are reachable via some combination of roads. You can also assume that the truck has infinite petrol and it needs not to refill during its trip.</p>

<p>Please help UN to find the best city to establish the relief base so that it minimizes the total distance traveled by the truck in a day.</p>

<h3>Input format</h3>
<p>Line 1: Three space-separated integers N, M, and K - number of cities, number of roads, and number of affected cities respectively.</p>
<p>Next, K line contains an integer in each line in the range 1...N identifying an affected city.</p>
<p>Next M lines: Each line contains three space separated integers u, v(1 <= u, v <= N), and l (1 <= l <= 1000) indicating the presence of a road between cities u and v.</p>
