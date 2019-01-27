package buttonclicker.controllers;

import buttonclicker.models.ClickResponse;
import buttonclicker.models.User;
import buttonclicker.models.Winner;
import buttonclicker.repositories.WinnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/")
public class GeneralController {
    @Value("${clicker.priceThreshold.small}")
    private long smallPriceThreshold;
    @Value("${clicker.priceThreshold.medium}")
    private long mediumPriceThreshold;
    @Value("${clicker.priceThreshold.large}")
    private long largePriceThreshold;
    @Value("${clicker.winnerList.size}")
    private int winnerListSize;

    @Autowired
    private WinnerRepository winnerRepository;

    private final AtomicLong clicks = new AtomicLong(0);

    @RequestMapping()
    public String getClickerPage() {
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ClickResponse click(HttpServletRequest request) {
        User currentUser = getUser(request);

        long currentClick = clicks.incrementAndGet();
        long[] clicksToWin = {
                largePriceThreshold,
                mediumPriceThreshold,
                smallPriceThreshold
        };
        long[] clickModulo = {
                currentClick % clicksToWin[0],
                currentClick % clicksToWin[1],
                currentClick % clicksToWin[2]
        };
        String price; // Todo: Localize

        if (clickModulo[0] == 0) {
            //someone won large price
            price = "large price";
        } else if (clickModulo[1] == 0) {
            // medium price
            price = "medium price";
        } else if (clickModulo[2] == 0) {
            //small price
            price = "small price";
        } else {
            price = null;
        }

        if (price != null) {
            winnerRepository.save(new Winner(
                    currentUser.getName(),
                    price,
                    Instant.now()
            ));
        }

        long clicksNeeded = Long.MAX_VALUE;
        for (int i = 0; i < clickModulo.length; i++) {
            // if clicks to win is 20 and current clicks are 2, modulo operator returns 2
            // we need to subtract that value from the clicks to win to get actual clicks needed
            long clicks = clicksToWin[i] - clickModulo[i];
            if (clicksNeeded > clicks)
                clicksNeeded = clicks;
        }

        return new ClickResponse(clicksNeeded, price);
    }

    @RequestMapping("/winners")
    @ResponseBody
    public List<Winner> getLatestWinners() {
        return winnerRepository.findAll(PageRequest.of(0, winnerListSize, Sort.Direction.DESC, "time")).getContent();
    }

    @RequestMapping("/name")
    @ResponseBody
    private User getUser(HttpServletRequest request) {
        User currentUser = (User) request.getSession(true).getAttribute("user");
        if (currentUser == null) {
            currentUser = new User();
            request.getSession().setAttribute("user", currentUser);
        }
        return currentUser;
    }
}
