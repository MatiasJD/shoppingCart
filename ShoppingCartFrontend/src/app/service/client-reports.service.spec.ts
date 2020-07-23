import { TestBed } from '@angular/core/testing';

import { ClientReportsService } from './client-reports.service';

describe('ClientReportsService', () => {
  let service: ClientReportsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientReportsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
