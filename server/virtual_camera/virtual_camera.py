import time

import ffmpeg
import numpy
import pyvirtualcam

LAST_FRAME = None
LAST_UPDATE = None
DELTA = []


def RGB2YUV(rgb):
    m = numpy.array([[0.29900, -0.16874, 0.50000],
                     [0.58700, -0.33126, -0.41869],
                     [0.11400, 0.50000, -0.08131]])

    yuv = numpy.dot(rgb, m)
    yuv[:, :, 1:] += 128.0
    return yuv


def cal_ti(frame):
    global LAST_FRAME
    global DELTA
    global LAST_UPDATE
    if LAST_FRAME is None:
        LAST_FRAME = RGB2YUV(frame)
        LAST_UPDATE = round(time.time() * 1000)
        return
    frame = RGB2YUV(frame)

    delta = numpy.std(LAST_FRAME[:, :, 0] - frame[:, :, 0])

    DELTA.append(delta)

    if round(time.time() * 1000) > LAST_UPDATE + 1000:
        t = min(DELTA) + (max(DELTA) - min(DELTA)) * 0.1
        c = 0
        for d in DELTA:
            if d > t:
                c += 1
        with open('fps', 'w') as f:
            f.write(str(c // len(DELTA)))

        LAST_UPDATE = round(time.time() * 1000)


def main():
    width = 640
    height = 480

    process1 = (
        ffmpeg
        .input('srt://127.0.0.1:10080?streamid=#!::r=live/livestream,m=request')
        .output('pipe:', format='rawvideo', pix_fmt='rgb24')
        .run_async(pipe_stdout=True)
    )

    with pyvirtualcam.Camera(width=width, height=height, fps=60, device='/dev/video2') as cam:
        while True:
            in_bytes = process1.stdout.read(width * height * 3)
            if not in_bytes:
                break
            in_frame = (
                numpy
                .frombuffer(in_bytes, numpy.uint8)
                .reshape([height, width, 3])
            )

            cam.send(in_frame)
            cam.sleep_until_next_frame()

    process1.wait()


if __name__ == '__main__':
    main()
