package com.epam.game.controller;

import com.epam.game.constants.AttributesEnum;
import com.epam.game.constants.GameType;
import com.epam.game.constants.ViewsEnum;
import com.epam.game.dao.GameDAO;
import com.epam.game.dao.UserDAO;
import com.epam.game.domain.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatisticsController {

    @Autowired
    GameDAO gameDao;
    @Autowired
    UserDAO userDao;


    @RequestMapping(value = "/" + ViewsEnum.GAME_STATISTICS_AJAX
            + ViewsEnum.EXTENSION, method = RequestMethod.GET)
    public String showGameStatistics(ModelMap model,
                                     @RequestParam(value = "id", required = false) Long id) {
        Game game = gameDao.getById(id);
        model.addAttribute(AttributesEnum.GAME_STATISTICS_AJAX, game);
        model.addAttribute(AttributesEnum.GAME_WINNER, userDao.getUserWith(game.getWinnerId()));
        return ViewsEnum.GAME_STATISTICS_AJAX;
    }

    @RequestMapping(value = "/" + ViewsEnum.STATISTICS + ViewsEnum.EXTENSION, method = RequestMethod.GET)
    public String showStatistics(ModelMap model) {
        model.addAttribute(AttributesEnum.STATISTICS,
                gameDao.getFinishedTournaments());
        return ViewsEnum.STATISTICS;
    }

	@RequestMapping(value = "/" + ViewsEnum.GAMES_STATISTICS
	                        + ViewsEnum.EXTENSION, method = RequestMethod.GET)
	public String showGamesStatistics(ModelMap map) {
		map.addAttribute(AttributesEnum.STATISTICS, gameDao.getStatistics());
//		List<CommonStatistics> commonStats = gameDao.getCommonStatistics();
		map.addAttribute("commonStatistics", gameDao.getCommonStatistics());

		for(GameType g: GameType.values()){
			map.addAttribute(g.name(),g.getMessage());
		}

//		for(GameType gt : GameType.values()) {
//			CommonStatistics cs = new CommonStatistics();
//			for(CommonStatistics c : commonStats) {
//				if(c.getType() == gt) cs = c;
//			}
//			map.addAttribute(gt.name(), cs);
//		}

		return ViewsEnum.GAMES_STATISTICS;
	}
}
