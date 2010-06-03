/* Bowling Example, Copyright 2003 Kyle Cordes
 * http://kylecordes.com
 * 
 * Code to render the state and progress of a Game in
 * HTML.  This one is testless, alas.
 * 
 * In addition, it only shows information at the frame
 * level, it doesn't have the little boxes at the top to 
 * show each roll.
 * 
 * This code is much less honed than the Game class; I 
 * intended it just to see how well Game's API supports
 * building a GUI.
 * 
 * Thus, I apologize for the poor mix of HTML and CSS.
 */

package bowling;

import java.io.*;

public class HtmlGameRenderer {

	private PrintWriter fw;

	public HtmlGameRenderer(String fileName) throws IOException {
		fw = new PrintWriter(new FileWriter(fileName));

		fw.println("<style type=\"text/css\">");
		fw.println("<!--");
		fw.println("#bowling table { border-collapse: collapse;	}");
		fw.println("#bowling td { width: 40; height: 40; font-family: sans-serif;");
		fw.println("font-size: medium; text-align: center; border: thin solid Black; }");
		fw.println("#bowling td.scoredcell { background: Yellow; }");
		fw.println("#bowling td.pendingcell { background: transparent; }");
		fw.println("#bowling td.activecell { background: Aqua; }");
		fw.println("#bowling td.emptycell {	background: transparent; }");
		fw.println("#bowling p { font-family: sans-serif; }");
		fw.println("#bowling td.bdet { width: 16; height: 15; font-size: xx-small; border: none   }");
		fw.println("-->");
		fw.println("</style>");
		fw.println("<div id=\"bowling\">");
	}

	public void printGame(Game game) throws ScoringException {
		fw.println("<table cellpadding=\"0\"><tr>");

		for (int f = 1; f <= 10; f++) {
			String cssClass;
			if (f <= game.scoredFrames()) {
				cssClass = "scoredcell";
			} else if (f <= game.finishedFrames()) {
				cssClass = "pendingcell";
			} else if (f == game.finishedFrames() + 1) {
				cssClass = "activecell";
			} else {
				cssClass = "emptycell";
			}
			
			fw.println("<td class=\"" + cssClass + "\" valign=\"top\">");
			
			fw.println("<table border=\"1\" width=\"40\">");
			fw.println("<tr>");
			
			int[] frameRolls = game.rollsForFrame(f);
			for(int roll=0; roll<3; roll++) {
				if(roll<frameRolls.length) {
					fw.println("<td class=\"bdet\">" + frameRolls[roll] + "</td>");
				} else {
					fw.println("<td class=\"bdet\"></td>");
				}
			}
			fw.println("</tr>");
			fw.println("</table>");

			if (f <= game.scoredFrames()) {
				fw.println(game.scoreForFrame(f));
			}
			
			fw.println("</td>");
		}
		fw.println("</tr></table>");
	}

	public void close() {
		fw.println("</div>");
		fw.close();
	}

	public void tellAboutPins(int pins) {
		fw.println("<p>Rolling... " + pins + " pins</p>");
	}
}
