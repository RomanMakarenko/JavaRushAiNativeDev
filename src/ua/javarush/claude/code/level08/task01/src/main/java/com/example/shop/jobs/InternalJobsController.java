package com.example.shop.jobs;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/jobs")
class InternalJobsController {

    private final JobRunner jobRunner;

    InternalJobsController(JobRunner jobRunner) {
        this.jobRunner = jobRunner;
    }

    // внутрішня точка входу: повторне надсилання завислих платежів.
    // Викликається планувальником, не призначена для публічних клієнтів.
    @PostMapping("/retry-payments")
    JobResult retryPayments() {
        return jobRunner.retryPayments();
    }
}