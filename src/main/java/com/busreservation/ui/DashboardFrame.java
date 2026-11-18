package com.busreservation.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import com.busreservation.model.Route;
import com.busreservation.model.Ticket;
import com.busreservation.service.BookingService;
import com.busreservation.service.BusService;
import com.busreservation.service.CancellationService;

public class DashboardFrame extends JFrame {

    private static final Color NAVY = new Color(18, 29, 49);
    private static final Color NAVY_2 = new Color(27, 42, 67);
    private static final Color BLUE = new Color(45, 105, 220);
    private static final Color BG = new Color(244, 247, 251);
    private static final Color TEXT = new Color(31, 43, 61);
    private static final Color MUTED = new Color(117, 130, 150);
    private static final Color BORDER = new Color(226, 232, 240);

    private final BusService busService = new BusService();
    private final BookingService bookingService = new BookingService();

    private final CardLayout pageLayout = new CardLayout();
    private final JPanel pageHost = new JPanel(pageLayout);

    private final JTextField from = field("Delhi");
    private final JTextField to = field("Chandigarh");
    private final JTextField date =
            field(LocalDate.now().plusDays(1).toString());

    private final JTextField passengerName = field("");
    private final JTextField age = field("");
    private final JTextField phone = field("");

    private final JComboBox<String> gender =
            new JComboBox<>(new String[]{"Male", "Female", "Other"});

    private final JTextField cancelId = field("");

    private final JPanel results = new JPanel();

    private final JPanel seatGrid =
            new JPanel(new GridLayout(0, 5, 9, 9));

    private final JLabel selectedBus =
            label("Select a bus to view seats", 13, MUTED);

    private final JLabel selectedSeatLabel =
            label("No seat selected", 13, MUTED);

    private final JLabel resultCount =
            label("Search a route to see available services", 12, MUTED);

    private final JLabel pageTitle =
            label("Overview", 25, TEXT);

    private Route activeRoute;
    private Integer activeSeat;

    private List<Integer> activeAvailable = List.of();

    private final Map<String, JButton> navButtons =
            new LinkedHashMap<>();

    public DashboardFrame() {

        setTitle("WayPoint | Bus Reservation System");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setMinimumSize(new Dimension(1120, 760));
        setSize(1440, 900);

        setLocationRelativeTo(null);

        getContentPane().setBackground(BG);

        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);

        right.add(buildTopbar(), BorderLayout.NORTH);

        pageHost.setOpaque(false);

        pageHost.add(buildOverview(), "Overview");
        pageHost.add(buildBookingPage(), "Find a bus");
        pageHost.add(buildCancellationPage(), "Cancel ticket");
        pageHost.add(buildHelpPage(), "Help & Support");

        right.add(pageHost, BorderLayout.CENTER);

        add(right, BorderLayout.CENTER);

        showPage("Overview");
    }

    // SIDEBAR

    private JPanel buildSidebar() {

        JPanel side = new JPanel();

        side.setBackground(NAVY);
        side.setPreferredSize(new Dimension(245, 0));

        side.setBorder(new EmptyBorder(26, 17, 22, 17));

        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

        JLabel brand = label("WAYPOINT", 21, Color.WHITE);

        brand.setFont(new Font("SansSerif", Font.BOLD, 21));

        JLabel sub = label(
                "BUS RESERVATION",
                10,
                new Color(151, 169, 195)
        );

        side.add(brand);
        side.add(Box.createVerticalStrut(4));
        side.add(sub);
        side.add(Box.createVerticalStrut(42));

        JLabel menu = label(
                "WORKSPACE",
                10,
                new Color(123, 144, 174)
        );

        menu.setBorder(new EmptyBorder(0, 10, 12, 0));

        side.add(menu);

        addNav(side, "Overview", "▦");
        addNav(side, "Find a bus", "⌕");
        addNav(side, "Cancel ticket", "↗");
        addNav(side, "Help & Support", "?");

        side.add(Box.createVerticalGlue());

        JPanel help = new JPanel();

        help.setBackground(NAVY_2);

        help.setBorder(new EmptyBorder(14, 13, 14, 13));

        help.setLayout(new BoxLayout(help, BoxLayout.Y_AXIS));

        help.add(label("Need assistance?", 13, Color.WHITE));

        help.add(Box.createVerticalStrut(6));

        help.add(label(
                "Manage journeys and bookings",
                10,
                new Color(166, 181, 202)
        ));

        side.add(help);

        side.add(Box.createVerticalStrut(15));

        side.add(label(
                "WAYPOINT DESKTOP  •  v1.0",
                9,
                new Color(113, 133, 162)
        ));

        return side;
    }

    private void addNav(JPanel side, String name, String icon) {

        JButton b = new JButton("  " + icon + "     " + name);

        b.setHorizontalAlignment(SwingConstants.LEFT);

        b.setFont(new Font("SansSerif", Font.PLAIN, 13));

        b.setForeground(new Color(194, 207, 225));
        b.setBackground(NAVY);

        b.setBorder(new EmptyBorder(13, 12, 13, 10));

        b.setFocusPainted(false);

        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        b.setAlignmentX(Component.LEFT_ALIGNMENT);

        b.addActionListener(e -> showPage(name));

        navButtons.put(name, b);

        side.add(b);

        side.add(Box.createVerticalStrut(5));
    }

    // TOPBAR

    private JPanel buildTopbar() {

        JPanel top = new JPanel(new BorderLayout());

        top.setBackground(Color.WHITE);

        top.setBorder(
                new CompoundBorder(
                        new MatteBorder(0, 0, 1, 0, BORDER),
                        new EmptyBorder(17, 30, 17, 30)
                )
        );

        pageTitle.setFont(
                new Font("SansSerif", Font.BOLD, 23)
        );

        top.add(pageTitle, BorderLayout.WEST);

        JPanel right = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 12, 0)
        );

        right.setOpaque(false);

        JLabel dateLabel = label(
                LocalDate.now().format(
                        DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")
                ),
                12,
                MUTED
        );

        JLabel avatar = label("VR", 12, Color.WHITE);

        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(BLUE);

        avatar.setPreferredSize(new Dimension(34, 34));

        avatar.setBorder(new LineBorder(BLUE, 1, true));

        right.add(dateLabel);
        right.add(avatar);

        top.add(right, BorderLayout.EAST);

        return top;
    }

    // OVERVIEW PAGE

    private JPanel buildOverview() {

        JPanel p = pagePanel();

        p.add(
                sectionHeading(
                        "Good day",
                        "Manage your bus journeys from one place."
                ),
                BorderLayout.NORTH
        );

        JPanel body = new JPanel();

        body.setOpaque(false);

        body.setLayout(
                new BoxLayout(body, BoxLayout.Y_AXIS)
        );

        JPanel stats = new JPanel(
                new GridLayout(1, 3, 16, 0)
        );

        stats.setOpaque(false);

        stats.add(statCard(
                "01",
                "BOOK A JOURNEY",
                "Search routes and reserve seats",
                BLUE
        ));

        stats.add(statCard(
                "02",
                "MANAGE BOOKINGS",
                "Cancel a ticket and check refunds",
                new Color(18, 154, 124)
        ));

        stats.add(statCard(
                "03",
                "TRAVEL SUPPORT",
                "Get help with bookings and journeys",
                new Color(141, 91, 210)
        ));

        body.add(stats);

        body.add(Box.createVerticalStrut(22));

        JPanel hero = new JPanel(
                new BorderLayout(18, 12)
        );

        hero.setBackground(NAVY_2);

        hero.setBorder(
                new EmptyBorder(27, 30, 27, 30)
        );

        hero.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 190)
        );

        JPanel copy = new JPanel();

        copy.setOpaque(false);

        copy.setLayout(
                new BoxLayout(copy, BoxLayout.Y_AXIS)
        );

        copy.add(label(
                "YOUR NEXT JOURNEY STARTS HERE",
                11,
                new Color(163, 190, 225)
        ));

        copy.add(Box.createVerticalStrut(12));

        JLabel heading = label(
                "Travel smarter. Book in minutes.",
                26,
                Color.WHITE
        );

        heading.setFont(
                new Font("SansSerif", Font.BOLD, 26)
        );

        copy.add(heading);

        copy.add(Box.createVerticalStrut(9));

        copy.add(label(
                "Find available buses, compare fares and choose your seat.",
                12,
                new Color(194, 207, 225)
        ));

        JButton go = primaryButton("Find a bus  →");

        go.addActionListener(
                e -> showPage("Find a bus")
        );

        hero.add(copy, BorderLayout.CENTER);
        hero.add(go, BorderLayout.EAST);

        body.add(hero);

        body.add(Box.createVerticalStrut(20));

        JPanel info = cardPanel();

        info.setLayout(new BorderLayout(10, 8));

        info.add(
                label("QUICK START", 12, MUTED),
                BorderLayout.NORTH
        );

        info.add(
                label(
                        "1. Search your source, destination and journey date. "
                                + "2. Select a bus and an available seat. "
                                + "3. Enter passenger details and confirm.",
                        13,
                        TEXT
                ),
                BorderLayout.CENTER
        );

        body.add(info);

        p.add(body, BorderLayout.CENTER);

        return p;
    }

    // BUS SEARCH AND BOOKING PAGE

    private JPanel buildBookingPage() {

        JPanel p = pagePanel();

        p.add(
                sectionHeading(
                        "Find your bus",
                        "Choose a route, compare services and reserve your seat."
                ),
                BorderLayout.NORTH
        );

        JPanel body = new JPanel(
                new BorderLayout(0, 16)
        );

        body.setOpaque(false);

        JPanel search = cardPanel();

        search.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(4, 8, 4, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        addSearchField(search, c, 0, "FROM", from);
        addSearchField(search, c, 1, "TO", to);
        addSearchField(search, c, 2, "JOURNEY DATE", date);

        JButton searchBtn = primaryButton("Search buses  →");

        searchBtn.addActionListener(
                e -> searchRoutes()
        );

        c.gridx = 3;
        c.gridy = 1;
        c.weightx = 0;

        search.add(searchBtn, c);

        JPanel left = new JPanel(
                new BorderLayout(0, 10)
        );

        left.setOpaque(false);

        JPanel heading = new JPanel(
                new BorderLayout()
        );

        heading.setOpaque(false);

        heading.add(
                label("Available services", 16, TEXT),
                BorderLayout.WEST
        );

        heading.add(resultCount, BorderLayout.EAST);

        left.add(heading, BorderLayout.NORTH);

        results.setOpaque(false);

        results.setLayout(
                new BoxLayout(results, BoxLayout.Y_AXIS)
        );

        JScrollPane scroll = new JScrollPane(results);

        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));

        scroll.getViewport().setOpaque(false);

        scroll.setOpaque(false);

        left.add(scroll, BorderLayout.CENTER);

        JPanel right = cardPanel();

        right.setPreferredSize(new Dimension(330, 0));

        right.setLayout(
                new BoxLayout(right, BoxLayout.Y_AXIS)
        );

        right.add(label("SEAT SELECTION", 11, MUTED));

        right.add(Box.createVerticalStrut(9));

        right.add(selectedBus);

        right.add(Box.createVerticalStrut(13));

        JPanel legend = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0)
        );

        legend.setOpaque(false);

        legend.add(dot(
                "Available",
                new Color(221, 242, 232)
        ));

        legend.add(dot(
                "Selected",
                new Color(219, 232, 255)
        ));

        right.add(legend);

        right.add(Box.createVerticalStrut(10));

        seatGrid.setOpaque(false);

        right.add(seatGrid);

        right.add(Box.createVerticalStrut(9));

        right.add(selectedSeatLabel);

        right.add(Box.createVerticalStrut(14));

        right.add(label("PASSENGER DETAILS", 11, MUTED));

        right.add(Box.createVerticalStrut(8));

        right.add(formLabel("Full name"));

        right.add(passengerName);

        right.add(Box.createVerticalStrut(8));

        JPanel two = new JPanel(
                new GridLayout(1, 2, 8, 0)
        );

        two.setOpaque(false);

        JPanel a = new JPanel();

        a.setOpaque(false);

        a.setLayout(
                new BoxLayout(a, BoxLayout.Y_AXIS)
        );

        a.add(formLabel("Age"));
        a.add(age);

        JPanel g = new JPanel();

        g.setOpaque(false);

        g.setLayout(
                new BoxLayout(g, BoxLayout.Y_AXIS)
        );

        g.add(formLabel("Gender"));
        g.add(gender);

        two.add(a);
        two.add(g);

        right.add(two);

        right.add(Box.createVerticalStrut(8));

        right.add(formLabel("Phone"));

        right.add(phone);

        right.add(Box.createVerticalStrut(13));

        JButton book = primaryButton("Confirm booking");

        book.setAlignmentX(Component.LEFT_ALIGNMENT);

        book.addActionListener(
                e -> bookTicket()
        );

        right.add(book);

        JPanel lower = new JPanel(
                new BorderLayout(16, 0)
        );

        lower.setOpaque(false);

        lower.add(left, BorderLayout.CENTER);
        lower.add(right, BorderLayout.EAST);

        body.add(search, BorderLayout.NORTH);
        body.add(lower, BorderLayout.CENTER);

        p.add(body, BorderLayout.CENTER);

        return p;
    }

    // CANCELLATION PAGE

    private JPanel buildCancellationPage() {

        JPanel p = pagePanel();

        p.add(
                sectionHeading(
                        "Cancel a ticket",
                        "Enter the ticket ID to cancel an existing reservation."
                ),
                BorderLayout.NORTH
        );

        JPanel card = cardPanel();

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setMaximumSize(
                new Dimension(560, 250)
        );

        card.add(label("TICKET CANCELLATION", 11, MUTED));

        card.add(Box.createVerticalStrut(12));

        card.add(formLabel("Ticket ID"));

        cancelId.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42)
        );

        card.add(cancelId);

        card.add(Box.createVerticalStrut(16));

        JButton b = primaryButton("Cancel ticket");

        b.setAlignmentX(Component.LEFT_ALIGNMENT);

        b.addActionListener(
                e -> cancelTicket()
        );

        card.add(b);

        JPanel wrap = new JPanel(
                new GridBagLayout()
        );

        wrap.setOpaque(false);

        wrap.add(card);

        p.add(wrap, BorderLayout.CENTER);

        return p;
    }

    // HELP AND SUPPORT PAGE

    private JPanel buildHelpPage() {

        JPanel p = pagePanel();

        p.add(
                sectionHeading(
                        "Help & Support",
                        "Find assistance with bookings, payments and journey information."
                ),
                BorderLayout.NORTH
        );

        JPanel content = new JPanel(
                new GridLayout(0, 2, 16, 16)
        );

        content.setOpaque(false);

        content.add(supportCard(
                "Booking assistance",
                "Search for buses, compare available services and select an available seat before confirming your booking."
        ));

        content.add(supportCard(
                "Ticket cancellation",
                "Open Cancel ticket and enter your ticket ID. The application will process the cancellation and show the refund amount."
        ));

        content.add(supportCard(
                "Passenger information",
                "Passenger details are used internally to create and manage reservations. Passenger manifests are not exposed in this interface."
        ));

        content.add(supportCard(
                "Need more help?",
                "For assistance with a booking, keep your ticket ID and journey date available when contacting the project administrator."
        ));

        p.add(content, BorderLayout.CENTER);

        return p;
    }

    private JPanel supportCard(String title, String description) {

        JPanel card = cardPanel();

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        JLabel heading = label(title, 15, TEXT);

        heading.setFont(
                new Font("SansSerif", Font.BOLD, 15)
        );

        card.add(heading);

        card.add(Box.createVerticalStrut(10));

        card.add(label(description, 12, MUTED));

        return card;
    }

    // UI COMPONENT HELPERS

    private JPanel pagePanel() {

        JPanel p = new JPanel(
                new BorderLayout(0, 22)
        );

        p.setOpaque(false);

        p.setBorder(
                new EmptyBorder(27, 30, 28, 30)
        );

        return p;
    }

    private JPanel sectionHeading(
            String title,
            String subtitle
    ) {

        JPanel h = new JPanel();

        h.setOpaque(false);

        h.setLayout(
                new BoxLayout(h, BoxLayout.Y_AXIS)
        );

        JLabel t = label(title, 25, TEXT);

        t.setFont(
                new Font("SansSerif", Font.BOLD, 25)
        );

        h.add(t);

        h.add(Box.createVerticalStrut(6));

        h.add(label(subtitle, 13, MUTED));

        return h;
    }

    private JPanel statCard(
            String number,
            String title,
            String desc,
            Color accent
    ) {

        JPanel p = cardPanel();

        p.setLayout(
                new BoxLayout(p, BoxLayout.Y_AXIS)
        );

        JLabel n = label(number, 25, accent);

        n.setFont(
                new Font("SansSerif", Font.BOLD, 25)
        );

        p.add(n);

        p.add(Box.createVerticalStrut(13));

        JLabel t = label(title, 11, TEXT);

        t.setFont(
                new Font("SansSerif", Font.BOLD, 11)
        );

        p.add(t);

        p.add(Box.createVerticalStrut(6));

        p.add(label(desc, 11, MUTED));

        return p;
    }

    private JPanel cardPanel() {

        JPanel p = new JPanel();

        p.setBackground(Color.WHITE);

        p.setBorder(
                new CompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(17, 18, 17, 18)
                )
        );

        return p;
    }

    private void addSearchField(
            JPanel p,
            GridBagConstraints c,
            int x,
            String title,
            JTextField f
    ) {

        JPanel box = new JPanel();

        box.setOpaque(false);

        box.setLayout(
                new BoxLayout(box, BoxLayout.Y_AXIS)
        );

        box.add(label(title, 10, MUTED));

        box.add(Box.createVerticalStrut(6));

        f.setPreferredSize(
                new Dimension(150, 40)
        );

        f.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 40)
        );

        box.add(f);

        c.gridx = x;
        c.gridy = 1;

        p.add(box, c);
    }

    private JLabel formLabel(String s) {

        return label(s, 11, MUTED);
    }

    private JPanel dot(String s, Color color) {

        JPanel p = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 4, 0)
        );

        p.setOpaque(false);

        JLabel d = label("●", 11, color);

        p.add(d);

        p.add(label(s, 10, MUTED));

        return p;
    }

    // NAVIGATION

    private void showPage(String name) {

        pageLayout.show(pageHost, name);

        pageTitle.setText(name);

        navButtons.forEach((k, b) -> {

            boolean active = k.equals(name);

            b.setBackground(
                    active
                            ? new Color(43, 64, 97)
                            : NAVY
            );

            b.setForeground(
                    active
                            ? Color.WHITE
                            : new Color(194, 207, 225)
            );
        });
    }

    // BUS SEARCH

    private void searchRoutes() {

        try {

            results.removeAll();

            seatGrid.removeAll();

            activeRoute = null;
            activeSeat = null;

            selectedBus.setText(
                    "Select a bus to view seats"
            );

            selectedSeatLabel.setText(
                    "No seat selected"
            );

            List<Route> routes = busService.searchRoutes(
                    from.getText(),
                    to.getText(),
                    LocalDate.parse(date.getText().trim())
            );

            resultCount.setText(
                    routes.size() + " service(s) found"
            );

            if (routes.isEmpty()) {

                results.add(
                        emptyState(
                                "No buses found",
                                "Try another route or journey date."
                        )
                );
            }

            for (Route r : routes) {

                results.add(routeCard(r));
            }

            results.revalidate();
            results.repaint();

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // BUS RESULT CARD

    private JPanel routeCard(Route r) {

        JPanel card = cardPanel();

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 150)
        );

        JPanel top = new JPanel(
                new BorderLayout()
        );

        top.setOpaque(false);

        JLabel name = label(
                r.getBus().getBusName(),
                15,
                TEXT
        );

        name.setFont(
                new Font("SansSerif", Font.BOLD, 15)
        );

        top.add(name, BorderLayout.WEST);

        top.add(
                label(
                        r.getBus().getBusType().replace('_', ' '),
                        10,
                        BLUE
                ),
                BorderLayout.EAST
        );

        card.add(top);

        card.add(Box.createVerticalStrut(13));

        JPanel times = new JPanel(
                new GridLayout(1, 3, 8, 0)
        );

        times.setOpaque(false);

        times.add(
                timeBlock(
                        r.getDepartureTime().toString(),
                        r.getSource()
                )
        );

        times.add(
                timeBlock("→", "Journey")
        );

        times.add(
                timeBlock(
                        r.getArrivalTime().toString(),
                        r.getDestination()
                )
        );

        card.add(times);

        card.add(Box.createVerticalStrut(11));

        JPanel bottom = new JPanel(
                new BorderLayout()
        );

        bottom.setOpaque(false);

        bottom.add(
                label(
                        "Bus #" + r.getBus().getBusId()
                                + "   •   "
                                + r.getBus().getTotalSeats()
                                + " seats",
                        10,
                        MUTED
                ),
                BorderLayout.WEST
        );

        JPanel action = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );

        action.setOpaque(false);

        JLabel price = label(
                "₹" + r.getPrice(),
                16,
                TEXT
        );

        price.setFont(
                new Font("SansSerif", Font.BOLD, 16)
        );

        JButton choose = smallButton("Choose seats");

        choose.addActionListener(
                e -> selectRoute(r)
        );

        action.add(price);
        action.add(choose);

        bottom.add(action, BorderLayout.EAST);

        card.add(bottom);

        card.add(Box.createVerticalStrut(4));

        return card;
    }

    private JPanel timeBlock(String t, String s) {

        JPanel p = new JPanel();

        p.setOpaque(false);

        p.setLayout(
                new BoxLayout(p, BoxLayout.Y_AXIS)
        );

        p.add(label(t, 16, TEXT));

        p.add(Box.createVerticalStrut(3));

        p.add(label(s, 10, MUTED));

        return p;
    }

    // SEAT SELECTION

    private void selectRoute(Route r) {

        try {

            activeRoute = r;

            activeSeat = null;

            activeAvailable = busService.getAvailableSeats(
                    r.getBus().getBusId(),
                    r.getBus().getTotalSeats(),
                    LocalDate.parse(date.getText().trim())
            );

            selectedBus.setText(
                    r.getBus().getBusName()
                            + " • "
                            + activeAvailable.size()
                            + " seats available"
            );

            selectedSeatLabel.setText(
                    "Choose a seat below"
            );

            seatGrid.removeAll();

            for (
                    int i = 1;
                    i <= r.getBus().getTotalSeats();
                    i++
            ) {

                final int n = i;

                JButton b = new JButton(
                        String.valueOf(i)
                );

                b.setPreferredSize(
                        new Dimension(43, 34)
                );

                b.setFont(
                        new Font("SansSerif", Font.BOLD, 11)
                );

                b.setFocusPainted(false);

                b.setBorder(
                        new LineBorder(BORDER, 1, true)
                );

                boolean available = activeAvailable.contains(i);

                b.setEnabled(available);

                b.setBackground(
                        available
                                ? new Color(231, 245, 238)
                                : new Color(246, 226, 226)
                );

                b.setForeground(
                        available
                                ? new Color(25, 125, 82)
                                : new Color(183, 82, 82)
                );

                b.addActionListener(e -> {

                    activeSeat = n;

                    selectedSeatLabel.setText(
                            "Selected seat: " + n
                                    + "   •   Fare: ₹" + r.getPrice()
                    );

                    for (Component x : seatGrid.getComponents()) {

                        if (x instanceof JButton btn) {

                            int val = Integer.parseInt(
                                    btn.getText()
                            );

                            btn.setBackground(
                                    val == n
                                            ? new Color(219, 232, 255)
                                            : new Color(231, 245, 238)
                            );

                            btn.setForeground(
                                    val == n
                                            ? BLUE
                                            : new Color(25, 125, 82)
                            );
                        }
                    }
                });

                seatGrid.add(b);
            }

            seatGrid.revalidate();
            seatGrid.repaint();

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // BOOKING

    private void bookTicket() {

        if (activeRoute == null || activeSeat == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select a bus and an available seat first."
            );

            return;
        }

        try {

            LocalDate d = LocalDate.parse(
                    date.getText().trim()
            );

            bookingService.bookSeat(
                    activeRoute.getBus().getBusId(),
                    activeSeat,
                    d
            );

            Ticket t = bookingService.confirmBooking(
                    activeRoute,
                    d,
                    activeSeat,
                    passengerName.getText(),
                    Integer.parseInt(age.getText().trim()),
                    (String) gender.getSelectedItem(),
                    phone.getText()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Booking confirmed\n"
                            + "Ticket ID: " + t.getTicketId() + "\n"
                            + "Passenger: " + t.getPassengerName() + "\n"
                            + "Seat: " + t.getSeatNumber() + "\n"
                            + "Amount: ₹" + t.getPrice(),
                    "Booking confirmed",
                    JOptionPane.INFORMATION_MESSAGE
            );

            selectRoute(activeRoute);

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // CANCELLATION

    private void cancelTicket() {

        try {

            long id = Long.parseLong(
                    cancelId.getText().trim()
            );

            BigDecimal refund =
                    CancellationService.cancel(id);

            JOptionPane.showMessageDialog(
                    this,
                    "Ticket cancelled successfully.\n"
                            + "Refund amount: ₹" + refund,
                    "Cancellation complete",
                    JOptionPane.INFORMATION_MESSAGE
            );

            cancelId.setText("");

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // EMPTY STATE

    private JPanel emptyState(
            String title,
            String subtitle
    ) {

        JPanel p = new JPanel();

        p.setBackground(Color.WHITE);

        p.setBorder(
                new CompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(25, 18, 25, 18)
                )
        );

        p.setLayout(
                new BoxLayout(p, BoxLayout.Y_AXIS)
        );

        p.add(label(title, 15, TEXT));

        p.add(Box.createVerticalStrut(6));

        p.add(label(subtitle, 11, MUTED));

        return p;
    }

    // INPUT FIELD

    private JTextField field(String value) {

        JTextField f = new JTextField(value);

        f.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        f.setForeground(TEXT);

        f.setBackground(
                new Color(250, 251, 253)
        );

        f.setCaretColor(BLUE);

        f.setBorder(
                new CompoundBorder(
                        new LineBorder(
                                new Color(215, 223, 234),
                                1,
                                true
                        ),
                        new EmptyBorder(9, 10, 9, 10)
                )
        );

        return f;
    }

    // LABEL

    private JLabel label(
            String s,
            int size,
            Color color
    ) {

        JLabel l = new JLabel(s);

        l.setFont(
                new Font("SansSerif", Font.PLAIN, size)
        );

        l.setForeground(color);

        return l;
    }

    // PRIMARY BUTTON

    private JButton primaryButton(String s) {

        JButton b = new JButton(s);

        b.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        b.setForeground(Color.WHITE);

        b.setBackground(BLUE);

        b.setBorder(
                new EmptyBorder(12, 18, 12, 18)
        );

        b.setFocusPainted(false);

        b.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return b;
    }

    // SMALL BUTTON

    private JButton smallButton(String s) {

        JButton b = new JButton(s);

        b.setFont(
                new Font("SansSerif", Font.BOLD, 10)
        );

        b.setForeground(BLUE);

        b.setBackground(
                new Color(238, 244, 255)
        );

        b.setBorder(
                new EmptyBorder(8, 11, 8, 11)
        );

        b.setFocusPainted(false);

        return b;
    }

    // ERROR HANDLING

    private void showError(Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage() == null
                        ? ex.toString()
                        : ex.getMessage(),
                "Something went wrong",
                JOptionPane.ERROR_MESSAGE
        );
    }
}