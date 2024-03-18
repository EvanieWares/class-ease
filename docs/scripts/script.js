$('document').ready(function () {
    addFeatures();
    addVideos();
});

class Feature {
    constructor(title, desc, imgUrl) {
        this.title = title;
        this.desc = desc;
        this.imgUrl = imgUrl;
    }
}

function addFeatures() {
    const features = [
        new Feature('Automatic grading', 'ClassEase automates grading, saving educators valuable time by calculating scores and ranking students effortlessly.', 'images/automatic_grading.png'),
        new Feature('Interactive reports', 'Empower educators with interactive reports that provide comprehensive insights into student performance. Visualize student progress, identify trends, and pinpoint areas for improvement through dynamic charts, fostering data-driven decision-making in the classroom.', 'images/interactive_reports.png'),
        new Feature('Intuitive Interface', 'ClassEase offers an intuitive interface designed for educators, making it easy to navigate and manage student records efficiently.', 'images/intuitive_interface.png')
    ];
    const $container = $('section#container');

    $.each(features, function(i, feature) {
        const $newFeature = $('<div>').addClass('feature').attr('data-aos', 'fade-up').attr('data-aos-delay', '320').attr('data-aos-duration', '1700');
        const $newTitleDescription = $('<div>').addClass('title-description');
        const $imgContainer = $('<div>').addClass('image-container');
        const $featureTitle = $('<h2>').text(feature.title);
        const $featureDescription = $('<p>').text(feature.desc);
        const $image = $('<img>').attr('src', feature.imgUrl).attr('alt', feature.title);

        $newTitleDescription.append($featureTitle, $featureDescription);
        $imgContainer.append($image);
        $newFeature.append($newTitleDescription, $imgContainer);
        $container.append($newFeature);

        if (i !== 2) {
            $container.append($('<hr>'));
        }
    });
}

function addVideos() {
    const videoIds = ['eP4j-794LDY']

    $.each(videoIds, function(i, videoId) {
        addVideo(videoId);
    });
}

function addVideo(videoId) {
    const videoUrl = `https://www.youtube.com/embed/${videoId}`;
    const $iframe = $('<iframe>', {
        'src': videoUrl,
        'frameborder': 0,
        'allowfullscreen': true
    }).addClass('how-to-video');

    $('#video-container').append($iframe);
}
