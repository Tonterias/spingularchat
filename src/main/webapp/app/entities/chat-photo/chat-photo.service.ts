import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IChatPhoto } from 'app/shared/model/chat-photo.model';

type EntityResponseType = HttpResponse<IChatPhoto>;
type EntityArrayResponseType = HttpResponse<IChatPhoto[]>;

@Injectable({ providedIn: 'root' })
export class ChatPhotoService {
  public resourceUrl = SERVER_API_URL + 'api/chat-photos';

  constructor(protected http: HttpClient) {}

  create(chatPhoto: IChatPhoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatPhoto);
    return this.http
      .post<IChatPhoto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(chatPhoto: IChatPhoto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(chatPhoto);
    return this.http
      .put<IChatPhoto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IChatPhoto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IChatPhoto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(chatPhoto: IChatPhoto): IChatPhoto {
    const copy: IChatPhoto = Object.assign({}, chatPhoto, {
      creationDate: chatPhoto.creationDate != null && chatPhoto.creationDate.isValid() ? chatPhoto.creationDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((chatPhoto: IChatPhoto) => {
        chatPhoto.creationDate = chatPhoto.creationDate != null ? moment(chatPhoto.creationDate) : null;
      });
    }
    return res;
  }
}
